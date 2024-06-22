package org.example.handler;

import org.example.in.CoworkingConsole;

import java.util.InputMismatchException;
import java.util.Scanner;

public class UserHandler {
    public Scanner scanner = new Scanner(System.in);

    CoworkingConsole coworkingConsole = new CoworkingConsole();

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



    public void logout() {
        coworkingConsole.setLoggedUsername(null);
        coworkingConsole.setLogged(false);
        System.out.println("Выход из аккаунта.");
    }

    public void displayUserMenu() {
        System.out.println("╔═════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║       Выберите действие:                                                ║");
        System.out.println("║  1. Cписок доступных рабочих мест и конференц-залов                     ║");
        System.out.println("║  2. Доступные слоты на определенную дату                                ║");
        System.out.println("║  3. Бронирование рабочего места или конференц-зала                      ║");
        System.out.println("║  4. Отмена бронирования                                                 ║");
        System.out.println("║  5. Просмотр всех бронирований                                          ║");
        System.out.println("║  6. Выйти из приложения                                                 ║");
        System.out.println("╚═════════════════════════════════════════════════════════════════════════╝");

    }

}
