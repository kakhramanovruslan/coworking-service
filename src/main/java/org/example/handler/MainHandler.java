package org.example.handler;

import org.example.utils.ScannerManager;

import java.util.InputMismatchException;
import java.util.Scanner;

public class MainHandler {

    private final Scanner scanner = ScannerManager.getInstance().scanner;

    public void displayMainMenu() {
        System.out.println("╔═════════════════════════════════════════════════╗");
        System.out.println("║         Выберите действие:                      ║");
        System.out.println("║ 1. Регистрация                                  ║");
        System.out.println("║ 2. Авторизация                                  ║");
        System.out.println("║ 3. Выйти из приложения                          ║");
        System.out.println("╚═════════════════════════════════════════════════╝");

    }

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

    public void exitApplication() {
        System.out.println("Выход из приложения.");
        System.exit(0);
    }
}
