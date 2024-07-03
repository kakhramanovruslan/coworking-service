package org.example.in;


import org.example.in.handler.AdminConsoleHandler;
import org.example.in.handler.MainConsoleHandler;
import org.example.in.handler.UserConsoleHandler;

/**
 * The CoworkingConsole class represents a text interface for interacting with the coworking service.
 * The user can perform registration, authentication, book workspaces and view available time,
 */
public class CoworkingConsole {

    public static String loggedUsername = null;
    public static boolean logged = false;

    /**
     * The start method launches a text interface for interacting with the coworking.
     * @param mainConsoleHandler General event handler
     * @param userConsoleHandler User event handler
     * @param adminConsoleHandler Admin event handler
     */
    public void start(MainConsoleHandler mainConsoleHandler, UserConsoleHandler userConsoleHandler, AdminConsoleHandler adminConsoleHandler) {
        while (true) {
            if (!logged) {
                mainConsoleHandler.displayMainMenu();
                int choice = mainConsoleHandler.readChoice();
                switch (choice) {
                    case 1:
                        userConsoleHandler.displayRegistration();
                        break;
                    case 2:
                        userConsoleHandler.displayAuthentication();
                        break;
                    case 3:
                        mainConsoleHandler.exitApplication();
                        break;
                    default:
                        System.out.println("Неверный выбор. Пожалуйста, выберите снова.");
                }
            } else if ("admin".equals(loggedUsername)) {
                adminConsoleHandler.displayAdminMenu();
                int choice = mainConsoleHandler.readChoice();
                switch (choice) {
                    case 1:
                        adminConsoleHandler.displayListOfAllWorkspaces();
                        break;
                    case 2:
                        adminConsoleHandler.displayWorkspace();
                        break;
                    case 3:
                        adminConsoleHandler.displayCreatingWorkspace();
                        break;
                    case 4:
                        adminConsoleHandler.displayUpdatingWorkspace();
                        break;
                    case 5:
                        adminConsoleHandler.displayDeletingWorkspace();
                        break;
                    case 6:
                        userConsoleHandler.logout();
                        break;
                    case 7:
                        mainConsoleHandler.exitApplication();
                        break;
                    default:
                        System.out.println("Неверный выбор. Пожалуйста, выберите снова.");
                }
            } else if (logged) {
                userConsoleHandler.displayUserMenu();
                int choice = mainConsoleHandler.readChoice();
                switch (choice) {
                    case 1:
                        userConsoleHandler.displayAvailableWorkspacesAtNow();
                        break;
                    case 2:
                        userConsoleHandler.displayAvailableWorkspacesForTimePeriod();
                        break;
                    case 3:
                        userConsoleHandler.displayBookingWorkspace();
                        break;
                    case 4:
                        userConsoleHandler.displayDeletingWorkspace();
                        break;
                    case 5:
                        userConsoleHandler.displayAllBookings();
                        break;
                    case 6:
                        userConsoleHandler.logout();
                        break;
                    case 7:
                        mainConsoleHandler.exitApplication();
                        break;
                    default:
                        System.out.println("Неверный выбор. Пожалуйста, выберите снова.");
                }

            }

        }
    }
}
