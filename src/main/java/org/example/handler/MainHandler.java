package org.example.handler;

public class MainHandler {

    public void displayMainMenu() {
        System.out.println("╔═════════════════════════════════════════════════╗");
        System.out.println("║         Выберите действие:                      ║");
        System.out.println("║ 1. Регистрация                                  ║");
        System.out.println("║ 2. Авторизация                                  ║");
        System.out.println("║ 3. Выйти из приложения                          ║");
        System.out.println("╚═════════════════════════════════════════════════╝");

    }

    public void exitApplication() {
        System.out.println("Выход из приложения.");
        System.exit(0);
    }
}
