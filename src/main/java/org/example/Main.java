package org.example;

import org.example.handler.AdminHandler;
import org.example.handler.MainHandler;
import org.example.handler.UserHandler;
import org.example.handler.WorkspaceHandler;
import org.example.in.CoworkingConsole;

public class Main {
    public static void main(String[] args) {
        MainHandler mainHandler = new MainHandler();
        UserHandler userHandler = new UserHandler();
        AdminHandler adminHandler = new AdminHandler();
        WorkspaceHandler workspaceHandler = new WorkspaceHandler();

        CoworkingConsole coworkingConsole = new CoworkingConsole();
        coworkingConsole.start(mainHandler, userHandler, adminHandler, workspaceHandler);
    }
}