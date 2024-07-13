package org.example.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utility class for managing database connections using JDBC.
 */
@Component
public class ConnectionManager {

    @Value("${db.url}")
    private String url;
    @Value("${db.driver-class-name}")
    private String driver;
    @Value("${db.username}")
    private String username;
    @Value("${db.password}")
    private String password;

    public Connection getConnection(String url, String username, String password, String driver) {
        try {
            this.url = url;
            this.driver = driver;
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
            Class.forName(driver);

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