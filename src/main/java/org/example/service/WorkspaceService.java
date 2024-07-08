package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.annotations.Auditable;
import org.example.dao.WorkspaceDao;
import org.example.dto.WorkspaceRequest;
import org.example.entity.Workspace;
import org.example.entity.types.ActionType;
import org.example.exceptions.WorkspaceAlreadyExistException;
import org.example.exceptions.WorkspaceNotFoundException;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing workspaces.
 */
@RequiredArgsConstructor
public class WorkspaceService {

    private final WorkspaceDao workspaceDao;

    /**
     * Retrieves a list of all workspaces.
     * @return List of all workspaces
     */
    public List<Workspace> getListOfAllWorkSpaces() {
        return workspaceDao.findAll();
    }

    /**
     * Creates a new workspace with the given name.
     * @param workspace Workspace request object containing the name
     * @return The created workspace object
     * @throws WorkspaceAlreadyExistException if a workspace with the same name already exists
     */
    @Auditable(actionType = ActionType.CREATE_WORKSPACE, username = "admin")
    public Workspace createWorkspace(WorkspaceRequest workspace) throws WorkspaceAlreadyExistException {
        Optional<Workspace> w = workspaceDao.findByName(workspace.getName());
        if (w.isPresent()) throw new WorkspaceAlreadyExistException("Workspace с таким имененм уже существует");
        Workspace newWorkspace = Workspace.builder().name(workspace.getName()).build();
        return workspaceDao.save(newWorkspace);
    }

    /**
     * Deletes a workspace by its name.
     * @param name Name of the workspace to delete
     * @return True if deletion was successful, false otherwise
     * @throws WorkspaceNotFoundException if the workspace with the specified name does not exist
     */
    @Auditable(actionType = ActionType.DELETE_WORKSPACE, username = "admin")
    public boolean deleteWorkspace(String name) throws WorkspaceNotFoundException{
        Workspace workspace = this.getWorkspace(name);
        return workspaceDao.deleteByName(workspace.getName());
    }

    /**
     * Deletes a workspace by its ID.
     * @param id ID of the workspace to delete
     * @return True if deletion was successful, false otherwise
     * @throws WorkspaceNotFoundException if the workspace with the specified ID does not exist
     */
    @Auditable(actionType = ActionType.DELETE_WORKSPACE, username = "admin")
    public boolean deleteWorkspace(Long id) {
        Workspace workspace = this.getWorkspace(id);
        return workspaceDao.deleteById(workspace.getId());
    }

    /**
     * Updates the name of a workspace.
     * @param oldName Current name of the workspace
     * @param workspace Updated workspace object with the new name
     * @return True if update was successful, false otherwise
     * @throws WorkspaceNotFoundException if the workspace with the specified old name does not exist
     * @throws WorkspaceAlreadyExistException if a workspace with the updated name already exists
     */
    @Auditable(actionType = ActionType.UPDATE_WORKSPACE, username = "admin")
    public boolean updateWorkspace(String oldName, Workspace workspace) throws WorkspaceNotFoundException, WorkspaceAlreadyExistException {
        Workspace oldWorkspace = this.getWorkspace(oldName);

        Optional<Workspace> newWorkspace = this.getWorkspaceByName(workspace.getName());
        if (newWorkspace.isPresent()) throw new WorkspaceAlreadyExistException("Workspace с таким именем уже существует");

        workspace.setId(oldWorkspace.getId());
        return workspaceDao.update(workspace);
    }

    /**
     * Retrieves a workspace by its name.
     * @param name Name of the workspace to retrieve
     * @return Optional containing the workspace if found, otherwise empty
     * @throws WorkspaceNotFoundException if the workspace with the specified name does not exist
     */
    public Optional<Workspace> getWorkspaceByName(String name) throws WorkspaceNotFoundException{
        this.getWorkspace(name);
        return workspaceDao.findByName(name);
    }

    /**
     * Retrieves a workspace by its ID.
     * @param id ID of the workspace to retrieve
     * @return Optional containing the workspace if found, otherwise empty
     * @throws WorkspaceNotFoundException if the workspace with the specified ID does not exist
     */
    public Optional<Workspace> getWorkspaceById(Long id) {
        this.getWorkspace(id);
        return workspaceDao.findById(id);
    }

    /**
     * Retrieves a workspace by its name.
     * @param name Name of the workspace to retrieve
     * @return Workspace object if found
     * @throws WorkspaceNotFoundException if the workspace with the specified name does not exist
     */
    public Workspace getWorkspace(String name) throws WorkspaceNotFoundException{
        return workspaceDao.findByName(name)
                .orElseThrow(() -> new WorkspaceNotFoundException("Workspace с таким именем не существует"));
    }

    /**
     * Retrieves a workspace by its ID.
     * @param id ID of the workspace to retrieve
     * @return Workspace object if found
     * @throws WorkspaceNotFoundException if the workspace with the specified ID does not exist
     */
    public Workspace getWorkspace(Long id) throws WorkspaceNotFoundException{
        return workspaceDao.findById(id)
                .orElseThrow(() -> new WorkspaceNotFoundException("Workspace с таким именем не существует"));
    }

}
