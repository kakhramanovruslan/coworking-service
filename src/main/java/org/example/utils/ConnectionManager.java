package org.example.utils;

import lombok.experimental.UtilityClass;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utility class for managing database connections using JDBC.
 */
@UtilityClass
public final class ConnectionManager {

    private final String URL = ConfigUtil.getProperty("db.url");
    private final String USERNAME = ConfigUtil.getProperty("db.username");
    private final String PASSWORD = ConfigUtil.getProperty("db.password");

    /**
     * Method for establishing connection with database.
     * @return Connection class which may be used to work with database.
     */
    public Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get a database connection.", e);
        }
    }
}