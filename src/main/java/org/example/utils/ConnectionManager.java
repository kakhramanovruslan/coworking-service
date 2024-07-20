package org.example.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utility class for managing database connections using JDBC.
 */
@Component
public class ConnectionManager {


    private String url = "jdbc:postgresql://localhost:5433/coworking_service_db";
    private String driverClassName = "org.postgresql.Driver";

    private String username = "ruslan";

    private String password = "123";

    /**
     * Method for establishing connection with database using values from parameter.
     * This method used for testing service and establish connection with test database.
     * @return Connection class which may be used to work with database.
     */
    public Connection getConnection(String url, String username, String password, String driver) {
        try {
            this.url = url;
            this.driverClassName = driver;
            this.password = password;
            this.username = username;
            Class.forName(driver);
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get a database connection.", e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method for establishing connection with database.
     * @return Connection class which may be used to work with database.
     */
    public Connection getConnection() {
        try {
            Class.forName(driverClassName);

            return DriverManager.getConnection(
                    url,
                    username,
                    password
            );
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get a database connection.", e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}