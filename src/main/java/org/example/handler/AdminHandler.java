package org.example.handler;

import org.example.entity.Workspace;
import org.example.service.WorkspaceService;
import org.example.utils.ScannerManager;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class AdminHandler {

    private final Scanner scanner = ScannerManager.getInstance().scanner;

    private WorkspaceService workspaceService = WorkspaceService.getInstance();


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

    public void displayListOfAllWorkspaces(){
        List<Workspace> workspaces = workspaceService.getListOfAllWorkSpaces();
        for (Workspace workspace : workspaces) {
            System.out.println("Id: "+workspace.getId() + ", Name: " + workspace.getName());
        }
    }

    public void displayWorkspace(){
        System.out.println("Введите имя workspace: ");
        String name = scanner.nextLine();
        Optional<Workspace> workspace = workspaceService.getWorkspace(name);
        if(workspace.isEmpty())
            System.out.println("Workspace с таким именем не существует");
        else
            System.out.println("Id: "+workspace.get().getId()+", Name: "+workspace.get().getName());
    }

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
