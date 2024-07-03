package org.example.in.handler;

import org.example.entity.Workspace;
import org.example.service.WorkspaceService;
import org.example.utils.ConnectionManager;
import org.example.utils.ScannerManager;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * The AdminConsoleHandler class is a handler for interacting with the admin menu of the
 * application console interface. It provides functionality related to workspace management.
 */

public class AdminConsoleHandler {

    private final Scanner scanner = ScannerManager.getInstance().scanner;

    private final WorkspaceService workspaceService;

    public AdminConsoleHandler(ConnectionManager connectionManager) {
        this.workspaceService = new WorkspaceService(connectionManager);
    }

    /**
     * Displays the menu for the administrator in the console.
     * The menu includes a list of available actions for the administrator.
     */
    public void displayAdminMenu(){
        System.out.println("---------------------------Меню администратора:----------------------------");
        System.out.println("╔═════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║       Выберите действие:                                                ║");
        System.out.println("║  1. Получить все доступные workspaces                                   ║");
        System.out.println("║  2. Получить подробную информацию конкретного workspace                 ║");
        System.out.println("║  3. Создать workspace                                                   ║");
        System.out.println("║  4. Обновить workspace                                                  ║");
        System.out.println("║  5. Удалить workspace                                                   ║");
        System.out.println("║  6. Выйти из аккаунта                                                   ║");
        System.out.println("║  7. Выйти из приложения                                                 ║");
        System.out.println("╚═════════════════════════════════════════════════════════════════════════╝");

    }

    /**
     * Displays menu for getting list of all names workspaces in the console.
     */
    public void displayListOfAllWorkspaces(){
        List<Workspace> workspaces = workspaceService.getListOfAllWorkSpaces();
        for (Workspace workspace : workspaces) {
            System.out.println("Id: "+workspace.getId() + ", Name: " + workspace.getName());
        }
    }

    /**
     * Displays menu for getting information about one selected workspace in the console.
     * The admin enters the name of the workspace to get.
     */
    public void displayWorkspace(){
        System.out.println("Введите имя workspace: ");
        String name = scanner.nextLine();
        Optional<Workspace> workspace = workspaceService.getWorkspace(name);
        if(workspace.isEmpty())
            System.out.println("Workspace с таким именем не существует");
        else
            System.out.println("Id: "+workspace.get().getId()+", Name: "+workspace.get().getName());
    }

    /**
     * Displays menu for creating workspace in the console.
     * The admin enters the information of the workspace to create.
     */
    public void displayCreatingWorkspace(){
        System.out.println("Для создания workspace введите следующие данные через пробел в точности, как показано ниже: ");
        System.out.println("Values: name");
        String[] row = scanner.nextLine().split(" ");
        if (workspaceService.createWorkspace(row[0]) != null){
            System.out.println("Создание workspace прошло успешно");
        } else {
            System.out.println("Упс...что то пошло не так, повторите попытку снова");
        };
    }

    /**
     * Displays menu for updating workspace in the console.
     * The admin enters the old name and new name of the workspace to update.
     */
    public void displayUpdatingWorkspace(){
        System.out.println("Для обновления в начале введите имя существующего workspace, затем через пробел введите новое значение имени");
        System.out.println("Values: oldName newName");
        String[] row = scanner.nextLine().split(" ");

        if (workspaceService.updateWorkspace(row[0], row[1])){
            System.out.println("Обновление workspace прошло успешно");
        } else {
            System.out.println("Упс...что то пошло не так, повторите попытку снова");
        };
    }

    /**
     * Displays menu for deleting workspace in the console.
     * The admin enters the name of the workspace to delete.
     */
    public void displayDeletingWorkspace() {
        System.out.println("Введите имя для удаления workspace: ");
        String name = scanner.nextLine();

        if (workspaceService.deleteWorkspace(name)){
            System.out.println("Удаление workspace прошло успешно");
        } else {
            System.out.println("Упс...что то пошло не так, повторите попытку снова");
        };
    }



}
