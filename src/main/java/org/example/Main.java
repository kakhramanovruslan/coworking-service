package org.example;

import org.example.handler.AdminHandler;
import org.example.handler.MainHandler;
import org.example.handler.UserHandler;
import org.example.in.CoworkingConsole;
import org.example.liquibase.LiquibaseDemo;
import org.example.utils.ConfigUtil;
import org.example.utils.ConnectionManager;

/**
 * The main class of the application that runs the console to work with the coworking.
 */
public class Main {

    /**
     * The main method that launches the application.
     */
    public static void main(String[] args) {
        String url = ConfigUtil.getProperty("db.url");
        String username = ConfigUtil.getProperty("db.username");
        String password = ConfigUtil.getProperty("db.password");
        ConnectionManager connectionManager = new ConnectionManager(url, username, password);

        MainHandler mainHandler = new MainHandler();
        UserHandler userHandler = new UserHandler(connectionManager);
        AdminHandler adminHandler = new AdminHandler(connectionManager);

        LiquibaseDemo liquibaseDemo = LiquibaseDemo.getInstance();
        liquibaseDemo.runMigrations(connectionManager.getConnection());

        CoworkingConsole coworkingConsole = new CoworkingConsole();
        coworkingConsole.start(mainHandler, userHandler, adminHandler);
    }
}