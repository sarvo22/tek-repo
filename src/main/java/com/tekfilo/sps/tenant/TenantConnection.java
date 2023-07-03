package com.tekfilo.sps.tenant;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Slf4j
@Service
public class TenantConnection {

    @Value("${postgresqlmasterdb.port}")
    private Integer masterDBPort;

    @Value("${postgresqlmasterdb.host}")
    private String masterDatabaseUrl;

    @Value("${postgresqlmasterdb.db}")
    private String masterDatabaseUsername;

    @Value("${postgresqlmasterdb.password}")
    private String masterDatabasePassword;

    @Value("${postgresqlmasterdb.driver-class-name}")
    private String driverClassName;


    public String executeTenantDatabaseScript(final String schemaName, List<String> sqlStatementsList) throws SQLException {
        Connection connection = createConnection();
        connection.setSchema(schemaName);
        connection.createStatement().execute("set schema '" + schemaName.toLowerCase() + "'");
        if (!connection.isClosed()) {

            Statement statement = connection.createStatement();
            for (String sql : sqlStatementsList) {
                statement.addBatch(sql);
            }
            int[] totalExecution = statement.executeBatch();
            log.info("Total Batch executed count {} " + totalExecution.length);
            connection.close();
        }
        return "Database Initiated Successfully";
    }

    private String getFileName(String fileName) {
        return fileName.startsWith("/") ? fileName : "/".concat(fileName);
    }

    /**
     * Creating the connection with Postgresql database
     *
     * @return
     */
    private Connection createConnection() {

        Connection connection = null;
        try {
            Class.forName(driverClassName);
            final String url = "jdbc:postgresql://" + masterDatabaseUrl + ":" + masterDBPort + "/" + masterDatabaseUsername;
            connection = DriverManager.getConnection(url, masterDatabaseUsername, masterDatabasePassword);
            System.out.println("Connection Created");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return connection;
    }
}
