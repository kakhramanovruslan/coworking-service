package org.example;

import org.example.in.handler.AdminConsoleHandler;
import org.example.in.handler.MainConsoleHandler;
import org.example.in.handler.UserConsoleHandler;
import org.example.in.CoworkingConsole;
import org.example.liquibase.LiquibaseManager;
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

        MainConsoleHandler mainConsoleHandler = new MainConsoleHandler();
        UserConsoleHandler userConsoleHandler = new UserConsoleHandler(connectionManager);
        AdminConsoleHandler adminConsoleHandler = new AdminConsoleHandler(connectionManager);

        LiquibaseManager liquibaseManager = LiquibaseManager.getInstance();
        liquibaseManager.runMigrations(connectionManager.getConnection());

        CoworkingConsole coworkingConsole = new CoworkingConsole();
        coworkingConsole.start(mainConsoleHandler, userConsoleHandler, adminConsoleHandler);
    }
}