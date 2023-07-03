package com.tekfilo.account.multitenancy;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.engine.jdbc.connections.internal.DriverManagerConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.AbstractMultiTenantConnectionProvider;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;


@Slf4j
public class SchemaMultiTenantConnectionProvider extends AbstractMultiTenantConnectionProvider {
    private static final String HIBERNATE_PROPERTIES_PROD_PATH = "/application-prod.properties";
    private static final String HIBERNATE_PROPERTIES_DEV_PATH = "/application-dev.properties";
    private static String HIBERNATE_PROPERTIES_PATH;
    private final Map<String, ConnectionProvider> connectionProviderMap;

    private String profile;


    public SchemaMultiTenantConnectionProvider() {
        try {
            this.profile = System.getProperty("spring.profiles.active");
            log.info("Profile setted {} " + this.profile);
        } catch (Exception exception) {
            this.profile = "dev";
        }


        this.connectionProviderMap = new HashMap<String, ConnectionProvider>();
    }

    @Override
    public Connection getConnection(String tenantIdentifier) throws SQLException {
        Connection connection = super.getConnection(tenantIdentifier);
        connection.createStatement().execute(String.format("SET SCHEMA '%s';", tenantIdentifier));
        return connection;
    }

    @Override
    protected ConnectionProvider getAnyConnectionProvider() {
        return getConnectionProvider(TenantContext.DEFAULT_TENANT_ID);
    }

    @Override
    protected ConnectionProvider selectConnectionProvider(String tenantIdentifier) {
        return getConnectionProvider(tenantIdentifier);
    }

    private ConnectionProvider getConnectionProvider(String tenantIdentifier) {
        return Optional.ofNullable(tenantIdentifier)
                .map(connectionProviderMap::get)
                .orElseGet(() -> createNewConnectionProvider(tenantIdentifier));
    }

    private ConnectionProvider createNewConnectionProvider(String tenantIdentifier) {
        return Optional.ofNullable(tenantIdentifier)
                .map(this::createConnectionProvider)
                .map(connectionProvider -> {
                    connectionProviderMap.put(tenantIdentifier, connectionProvider);
                    return connectionProvider;
                })
                .orElseThrow(() -> new RuntimeException(String.format("Cannot create new connection provider for tenant: %s", tenantIdentifier)));
    }

    private ConnectionProvider createConnectionProvider(String tenantIdentifier) {
        return Optional.ofNullable(tenantIdentifier)
                .map(this::getHibernatePropertiesForTenantId)
                .map(this::initConnectionProvider)
                .orElse(null);
    }

    private Properties getHibernatePropertiesForTenantId(String tenantId) {
        try {
            Properties properties = new Properties();
            HIBERNATE_PROPERTIES_PATH = getProfileProperty();
            properties.load(getClass().getResourceAsStream(HIBERNATE_PROPERTIES_PATH));
            return properties;
        } catch (IOException e) {
            throw new RuntimeException(String.format("Cannot open hibernate properties: %s)", HIBERNATE_PROPERTIES_PATH));
        }
    }

    private String getProfileProperty() {
        if (this.profile == null)
            return HIBERNATE_PROPERTIES_DEV_PATH;
        if (this.profile.equalsIgnoreCase("dev"))
            return HIBERNATE_PROPERTIES_DEV_PATH;
        if (this.profile.equalsIgnoreCase("prod"))
            return HIBERNATE_PROPERTIES_PROD_PATH;
        return HIBERNATE_PROPERTIES_DEV_PATH;
    }

    private ConnectionProvider initConnectionProvider(Properties hibernateProperties) {
        DriverManagerConnectionProviderImpl connectionProvider = new DriverManagerConnectionProviderImpl();
        connectionProvider.configure(hibernateProperties);
        return connectionProvider;
    }
}
