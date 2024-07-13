package org.example.config;

import liquibase.integration.spring.SpringLiquibase;
import org.example.utils.YamlPropertySourceFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Configuration class for setting up data source and Liquibase integration using Spring.
 */
@Configuration
@PropertySource(value = "classpath:application.yml", factory = YamlPropertySourceFactory.class)
public class DataSourceConfig {

    @Value("${db.url}")
    private String url;

    @Value("${db.driver-class-name}")
    private String driver;

    @Value("${db.username}")
    private String username;

    @Value("${db.password}")
    private String password;

    @Value("${liquibase.changeLogFile}")
    private String changeLogFile;

    @Value("${liquibase.schemaName}")
    private String schemaName;

    /**
     * Configures and provides a JdbcTemplate bean initialized with the configured data source.
     *
     * @return configured JdbcTemplate bean
     */
    @Bean
    public JdbcTemplate jdbcTemplate() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driver);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            statement.execute("CREATE SCHEMA IF NOT EXISTS " + schemaName);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return new JdbcTemplate(dataSource);
    }

    /**
     * Configures and provides a SpringLiquibase bean initialized with the configured Liquibase settings.
     *
     * @return configured SpringLiquibase bean
     */
    @Bean
    public SpringLiquibase liquibase() {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setLiquibaseSchema(schemaName);
        liquibase.setChangeLog(changeLogFile);
        liquibase.setDataSource(jdbcTemplate().getDataSource());
        return liquibase;
    }
}
