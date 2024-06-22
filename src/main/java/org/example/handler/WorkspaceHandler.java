package org.example.handler;

import org.example.entity.Workspace;
import org.example.service.CoworkingService;

import java.util.List;

public class WorkspaceHandler {
    public void displayListOfAllWorkspaces(List<Workspace> workspaces){
        for (Workspace workspace : workspaces) {
            System.out.println("Id: "+workspace.getId() + ", Name: " + workspace.getName());
        }
    }

    public void displayWorkspace(CoworkingService coworkingService, UserHandler userHandler){
        System.out.println("Введите имя workspace: ");
        String workspaceName = userHandler.scanner.nextLine();
        Workspace workspace = coworkingService.getWorkspace(workspaceName);
        System.out.println("Подробная информация: \nId: "+workspace.getId() + ", Name: " + workspace.getName());
    }

    public void createWorkspaceMenu(CoworkingService coworkingService, UserHandler userHandler){
        System.out.println("Для создания workspace введите следующие данные через пробел в точности, как показано ниже: ");
        System.out.println("Types: String");
        System.out.println("Values: name");
        String[] row = userHandler.scanner.nextLine().split(" ");
        if (coworkingService.createWorkspace(row[0])){
            System.out.println("Создание workspace прошло успешно, идентификатор workspace");
        } else {
            System.out.println("Упс...что то пошло не так, повторите попытку снова");
        };
    }

    public void updateWorkspaceMenu(CoworkingService coworkingService, UserHandler userHandler){
        System.out.println("Для обновления в начале введите id существующего workspace, затем через пробел перечислите новые значения полей как показано ниже");
        System.out.println("Types: Integer String");
        System.out.println("Values: id name");
        String[] row = userHandler.scanner.nextLine().split(" ");

        if (coworkingService.updateWorkspace(Long.parseLong(row[0]), row[1])){
            System.out.println("Обновление workspace прошло успешно");
        } else {
            System.out.println("Упс...что то пошло не так, повторите попытку снова");
        };
    }

    public void deleteWorkspaceMenu(CoworkingService coworkingService, UserHandler userHandler) {
        System.out.println("Введите имя для удаления workspace: ");
        String name = userHandler.scanner.nextLine();

        if (coworkingService.deleteWorkspace(name)){
            System.out.println("Удаление workspace прошло успешно");
        } else {
            System.out.println("Упс...что то пошло не так, повторите попытку снова");
        };
    }
}
