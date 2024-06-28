package org.example.service;

import org.example.dao.impl.WorkspaceDaoImpl;
import org.example.entity.Workspace;

import java.util.List;
import java.util.Optional;

public class WorkspaceService {
    private static WorkspaceService workspaceService = new WorkspaceService();

    private final WorkspaceDaoImpl workspaceDao = WorkspaceDaoImpl.getInstance();


    public List<Workspace> getListOfAllWorkSpaces() {
        return workspaceDao.findAll();
    }

    public Workspace createWorkspace(String name) {
        Workspace newWorkspace = workspaceDao.save(Workspace.builder()
                                   .name(name)
                                   .build());
        return newWorkspace;
    }

    public boolean deleteWorkspace(String name) {
        return workspaceDao.deleteByName(name);
    }

    public boolean deleteWorkspace(Long id) {
        return workspaceDao.deleteById(id);
    }

    public boolean updateWorkspace(String oldName, String newName) {
        Optional<Workspace> oldWorkspace = workspaceDao.findByName(oldName);
        if(oldWorkspace.isPresent()){
            Workspace newWorkspace = Workspace.builder()
                    .id(oldWorkspace.get().getId())
                    .name(newName)
                    .build();
            return workspaceDao.update(newWorkspace);
        }
        return false;
    }

    public Optional<Workspace> getWorkspace(String name) {
        return workspaceDao.findByName(name);
    }

    public Optional<Workspace> getWorkspace(Long id) {
        return workspaceDao.findById(id);
    }

    public static WorkspaceService getInstance() {
        return workspaceService;
    }

}
