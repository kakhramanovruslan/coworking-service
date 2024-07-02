package org.example.handler;

import org.example.service.WorkspaceService;
import org.example.utils.ConnectionManager;
import org.example.utils.ScannerManager;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * The MainHandler class is a handler for interacting with the main menu of the
 * application console interface. It provides functionality related to displaying
 * the main menu and handling actions related to it.
 */
public class MainHandler {

    private final Scanner scanner = ScannerManager.getInstance().scanner;

    /**
     * Displays the main application menu in the console.
     */
    public void displayMainMenu() {
        System.out.println("╔═════════════════════════════════════════════════╗");
        System.out.println("║         Выберите действие:                      ║");
        System.out.println("║ 1. Регистрация                                  ║");
        System.out.println("║ 2. Авторизация                                  ║");
        System.out.println("║ 3. Выйти из приложения                          ║");
        System.out.println("╚═════════════════════════════════════════════════╝");

    }

    /**
     * Reads the user's choice from console.
     * @return The user's choice.
     */
    public int readChoice() {
        int choice = 0;
        try {
            choice = scanner.nextInt();
            scanner.nextLine();
        } catch (InputMismatchException e) {
            scanner.nextLine();
        }
        return choice;
    }

    /**
     * Completes the execution of the application.
     */
    public void exitApplication() {
        System.out.println("Выход из приложения.");
        System.exit(0);
    }
}
