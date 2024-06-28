package org.example.controller;

import org.example.entity.Workspace;
import org.example.service.WorkspaceService;

import java.util.List;
import java.util.Optional;

public class WorkspaceController {
    private static WorkspaceController workspaceController = new WorkspaceController();
    private WorkspaceService workspaceService = WorkspaceService.getInstance();

    public void getListOfAllWorkSpaces() {
        List<Workspace> workspaces = workspaceService.getListOfAllWorkSpaces();
        for (Workspace workspace : workspaces) {
            System.out.println("Id: "+workspace.getId() + ", Name: " + workspace.getName());
        }
    }

    public void createWorkspace(String name) {
        if(workspaceService.createWorkspace(name)!=null)
            System.out.println("Создание workspace прошло успешно, идентификатор workspace");
        else
            System.out.println("Упс...что то пошло не так, повторите попытку снова");
    }

    public void deleteWorkspace(String name){
        if(workspaceService.deleteWorkspace(name))
            System.out.println("Удаление workspace прошло успешно");
        else
            System.out.println("Упс...что то пошло не так, повторите попытку снова");
    }

    public void deleteWorkspace(Long id){
        if(workspaceService.deleteWorkspace(id))
            System.out.println("Удаление workspace прошло успешно");
        else
            System.out.println("Упс...что то пошло не так, повторите попытку снова");
    }

    public void updateWorkspace(String oldName, String newName){
        if(workspaceService.updateWorkspace(oldName, newName))
            System.out.println("Обновление workspace прошло успешно");
        else
            System.out.println("Упс...что то пошло не так, повторите попытку снова");
    }

    public void getWorkspace(String name){
        Optional<Workspace> workspace = workspaceService.getWorkspace(name);
        if(workspace.isEmpty())
            System.out.println("Workspace с таким именем не существует");
        else
            System.out.println("Id: "+workspace.get().getId()+", Name: "+workspace.get().getName());
    }



    public static WorkspaceController getInstance() {
        return workspaceController;
    }

}
