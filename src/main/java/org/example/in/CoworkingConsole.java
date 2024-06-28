package org.example.in;


import org.example.handler.AdminHandler;
import org.example.handler.MainHandler;
import org.example.handler.UserHandler;
import org.example.service.UserService;
import org.example.utils.ScannerManager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class CoworkingConsole {

    private final UserService userService = UserService.getInstance();
    public static String loggedUsername = null; // username активного пользователя
    public static boolean logged = false; // отслеживание авторизации

    Scanner scanner = ScannerManager.getInstance().scanner;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public void start(MainHandler mainHandler, UserHandler userHandler, AdminHandler adminHandler) {
        while (true) {
            if (!logged) {
                mainHandler.displayMainMenu();
                int choice = mainHandler.readChoice();
                switch (choice) {
                    case 1:
                        userHandler.displayRegistration();
                        break;
                    case 2:
                        userHandler.displayAuthentication();
                        break;
                    case 3:
                        mainHandler.exitApplication();
                        break;
                    default:
                        System.out.println("Неверный выбор. Пожалуйста, выберите снова.");
                }
            } else if ("admin".equals(loggedUsername)) {
                adminHandler.displayAdminMenu();
                int choice = mainHandler.readChoice();
                switch (choice) {
                    case 1:
                        adminHandler.displayListOfAllWorkspaces();
                        break;
                    case 2:
                        adminHandler.displayWorkspace();
                        break;
                    case 3:
                        adminHandler.displayCreatingWorkspace();
                        break;
                    case 4:
                        adminHandler.displayUpdatingWorkspace();
                        break;
                    case 5:
                        adminHandler.displayDeletingWorkspace();
                        break;
                    case 6:
                        userHandler.logout();
                        break;
                    case 7:
                        mainHandler.exitApplication();
                        break;
                    default:
                        System.out.println("Неверный выбор. Пожалуйста, выберите снова.");
                }
            } else if (logged) {
                userHandler.displayUserMenu();
                int choice = mainHandler.readChoice();
                switch (choice) {
                    case 1:
                        userHandler.displayAvailableWorkspacesAtNow();
                        break;
                    case 2:
                        userHandler.displayAvailableWorkspacesForTimePeriod();
                        break;
                    case 3:
                        userHandler.displayBookingWorkspace();
                        break;
                    case 4:
                        userHandler.displayDeletingWorkspace();
                        break;
                    case 5:
                        userHandler.displayAllBookings();
                        break;
                    case 6:
                        userHandler.logout();
                        break;
                    case 7:
                        mainHandler.exitApplication();
                        break;
                    default:
                        System.out.println("Неверный выбор. Пожалуйста, выберите снова.");
                }

            }

        }
    }
}
