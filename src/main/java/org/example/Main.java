package org.example;

import org.example.handler.AdminHandler;
import org.example.handler.MainHandler;
import org.example.handler.UserHandler;
import org.example.in.CoworkingConsole;
import org.example.liquibase.LiquibaseDemo;

public class Main {
    public static void main(String[] args) {
        MainHandler mainHandler = new MainHandler();
        UserHandler userHandler = new UserHandler();
        AdminHandler adminHandler = new AdminHandler();

        LiquibaseDemo liquibaseDemo = LiquibaseDemo.getInstance();
        liquibaseDemo.runMigrations();

        CoworkingConsole coworkingConsole = new CoworkingConsole();
        coworkingConsole.start(mainHandler, userHandler, adminHandler);
    }
}