package org.example.utils;

import lombok.experimental.UtilityClass;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


@UtilityClass
public class ConnectionManager {
    private final String URL = "jdbc:postgresql://localhost:5433/coworking_service_db";
    private final String USER_NAME = "ruslan";
    private final String PASSWORD = "123";

    public Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER_NAME, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get a database connection.", e);
        }
    }
}