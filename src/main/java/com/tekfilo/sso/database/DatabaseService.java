package com.tekfilo.sso.database;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import javax.transaction.Transactional;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@Slf4j
@Service
@Transactional
public class DatabaseService {


    @Value("${database.script.trigger-filename}")
    String userTrigerFileName;
    @Value("${database.script.view-filename}")
    String userViewFileName;
    @Autowired
    DataSource dataSource;
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
    @Value("${database.script.schema-script}")
    private String schemaScript;
    @Value("${database.script.user-ddl-name}")
    private String userDDLFileName;

    public void initTenant(Integer userId, String uid) throws Exception {
        log.info("Creating Teanant Database Configuration for  : " + userId + " Unique UID : " + uid);
        log.info("This may take while to update the required information, Please wait until it complete");
        initSchema(uid);
        initDatabase(uid, userDDLFileName);
        //initDatabase(uid, userTrigerFileName);
        //(uid, userViewFileName);
    }

    public void initSchema(final String schemaName) throws SQLException {
        Connection connection = createConnection();
        if (!connection.isClosed()) {
            Statement statement = connection.createStatement();
            statement.execute(schemaScript.replace("{schema}", schemaName));
            statement.close();
        }
        connection.close();
    }

    /**
     * Initilize the data as required by front end user
     *
     * @param schemaName
     * @param fileName
     * @return
     * @throws SQLException
     */

    public String initDatabase(final String schemaName, String fileName) throws SQLException {
        Connection connection = createConnection();
        connection.setSchema(schemaName);
        connection.createStatement().execute("set schema '" + schemaName.toLowerCase() + "'");
        if (!connection.isClosed()) {
            ScriptRunner scriptRunner = new ScriptRunner(connection);
            scriptRunner.setStopOnError(true);
            InputStream is = getClass().getResourceAsStream(getFileName(fileName));
            Reader reader = new InputStreamReader(is);
            System.out.println("Current Schema applied here is : " + connection.getSchema());
            try {
                scriptRunner.runScript(reader);
            } catch (Exception exception) {
                log.error("exception from init database :: " + exception.getMessage());
                connection.createStatement().execute("DROP SCHEMA " + schemaName + " CASCADE");
            }
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

    public void createData(final String schemaName, String fileName) throws SQLException {
        this.initDatabase(schemaName, fileName);
    }


    public void createTenantDatabase(String schemaName) throws Exception {
        Flyway flyway = Flyway.configure()
                .validateOnMigrate(false)
                .baselineOnMigrate(true)
                .locations("db/migration/schema/tenants")
                .dataSource(dataSource)
                .schemas(schemaName.toLowerCase())
                .connectRetries(10)
                .load();

        flyway.migrate();
    }

    public void createTenantTriggers(String schemaName) throws Exception {
        Flyway flyway = Flyway.configure()
                .validateOnMigrate(false)
                .baselineOnMigrate(true)
                .locations("db/migration/schema/trigger")
                .dataSource(dataSource)
                .schemas(schemaName.toLowerCase())
                .connectRetries(10)
                .load();
        flyway.migrate();
    }

}
