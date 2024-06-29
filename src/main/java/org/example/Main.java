package org.example;

import org.example.handler.AdminHandler;
import org.example.handler.MainHandler;
import org.example.handler.UserHandler;
<<<<<<< HEAD
import org.example.in.CoworkingConsole;
import org.example.liquibase.LiquibaseDemo;

/**
 * The main class of the application that runs the console to work with the coworking.
 */
public class Main {

    /**
     * The main method that launches the application.
     */
=======
import org.example.handler.WorkspaceHandler;
import org.example.in.CoworkingConsole;

public class Main {
>>>>>>> 43e3611c8f9b95f07c7653312542905fd21780d8
    public static void main(String[] args) {
        MainHandler mainHandler = new MainHandler();
        UserHandler userHandler = new UserHandler();
        AdminHandler adminHandler = new AdminHandler();
<<<<<<< HEAD

        LiquibaseDemo liquibaseDemo = LiquibaseDemo.getInstance();
        liquibaseDemo.runMigrations();

        CoworkingConsole coworkingConsole = new CoworkingConsole();
        coworkingConsole.start(mainHandler, userHandler, adminHandler);
=======
        WorkspaceHandler workspaceHandler = new WorkspaceHandler();

        CoworkingConsole coworkingConsole = new CoworkingConsole();
        coworkingConsole.start(mainHandler, userHandler, adminHandler, workspaceHandler);
>>>>>>> 43e3611c8f9b95f07c7653312542905fd21780d8
    }
}