package org.example.handler;

public class AdminHandler {

    public void displayAdminMenu(){
        System.out.println("---------------------------Меню администратора:----------------------------");
        System.out.println("╔═════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║       Выберите действие:                                                ║");
        System.out.println("║  1. Получить все доступные workspaces                                   ║");
        System.out.println("║  2. Получить подробную информацию конкретного workspace                 ║");
        System.out.println("║  3. Создать workspace                                                   ║");
        System.out.println("║  4. Обновить workspace                                                  ║");
        System.out.println("║  5. Удалить workspace                                                   ║");
        System.out.println("║  6. Выйти из приложения                                                 ║");
        System.out.println("╚═════════════════════════════════════════════════════════════════════════╝");

    }

}
