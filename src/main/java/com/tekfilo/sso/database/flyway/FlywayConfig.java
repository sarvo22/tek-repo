package com.tekfilo.sso.database.flyway;

import com.tekfilo.sso.tenantdbconfig.TenantDBConfigEntity;
import com.tekfilo.sso.tenantdbconfig.TenantDBConfigRepository;
import org.flywaydb.core.Flyway;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class FlywayConfig {

    static final String DEFAULT_TENANT = "public";

    @Bean
    public Flyway flyway(DataSource dataSource) {
        Flyway flyway = Flyway.configure()
                .baselineOnMigrate(true)
                .locations("db/migration/default")
                .dataSource(dataSource)
                .schemas(DEFAULT_TENANT)
                .load();
        flyway.repair();
        flyway.migrate();
        return flyway;
    }

    @Bean
    CommandLineRunner commandLineRunner(TenantDBConfigRepository repository, DataSource dataSource) {
        return args -> {
            List<String> tenants = repository.findAll().stream().filter(e -> e.getIsLocked() == 0).map(TenantDBConfigEntity::getTenantUID).distinct().collect(Collectors.toList());
            tenants.stream().forEach(user -> {
                String tenant = user.toLowerCase();
                Flyway flyway = Flyway.configure()
                        .validateOnMigrate(false)
                        .baselineOnMigrate(true)
                        .locations("db/migration/schema")
                        .dataSource(dataSource)
                        .schemas(tenant)
                        .connectRetries(10)
                        .load();
                flyway.repair();
                flyway.migrate();
            });
        };
    }
}
