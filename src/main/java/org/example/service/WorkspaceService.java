package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dao.impl.UserDaoImpl;
import org.example.dao.impl.WorkspaceDaoImpl;
import org.example.entity.Workspace;
import org.example.utils.ConnectionManager;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing workspaces.
 */
public class WorkspaceService {

    private final WorkspaceDaoImpl workspaceDao;

    public WorkspaceService(ConnectionManager connectionManager) {
        this.workspaceDao = new WorkspaceDaoImpl(connectionManager);
    }

    /**
     * Retrieves a list of all workspaces.
     * @return List of all workspaces
     */
    public List<Workspace> getListOfAllWorkSpaces() {
        return workspaceDao.findAll();
    }

    /**
     * Creates a new workspace with the given name.
     * @param name Name of the new workspace
     * @return The created workspace object
     */
    public Workspace createWorkspace(String name) {
        Workspace newWorkspace = workspaceDao.save(Workspace.builder()
                                   .name(name)
                                   .build());
        return newWorkspace;
    }

    /**
     * Deletes a workspace by its name.
     * @param name Name of the workspace to delete
     * @return True if deletion was successful, false otherwise
     */
    public boolean deleteWorkspace(String name) {
        return workspaceDao.deleteByName(name);
    }

    /**
     * Deletes a workspace by its ID.
     * @param id ID of the workspace to delete
     * @return True if deletion was successful, false otherwise
     */
    public boolean deleteWorkspace(Long id) {
        return workspaceDao.deleteById(id);
    }

    /**
     * Updates the name of a workspace.
     * @param oldName Current name of the workspace
     * @param newName New name to update
     * @return True if update was successful, false otherwise
     */
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

    /**
     * Retrieves a workspace by its name.
     * @param name Name of the workspace to retrieve
     * @return Optional containing the workspace if found, otherwise empty
     */
    public Optional<Workspace> getWorkspace(String name) {
        return workspaceDao.findByName(name);
    }

    /**
     * Retrieves a workspace by its ID.
     * @param id ID of the workspace to retrieve
     * @return Optional containing the workspace if found, otherwise empty
     */
    public Optional<Workspace> getWorkspace(Long id) {
        return workspaceDao.findById(id);
    }

}
