package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.annotations.Auditable;
import org.example.repository.WorkspaceRepository;
import org.example.dto.WorkspaceRequest;
import org.example.entity.Workspace;
import org.example.entity.types.ActionType;
import org.example.exceptions.WorkspaceAlreadyExistException;
import org.example.exceptions.WorkspaceNotFoundException;
import org.example.utils.ValidationUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing workspaces.
 */
@Service
@RequiredArgsConstructor
public class WorkspaceService {

    private final WorkspaceRepository workspaceRepository;

    /**
     * Retrieves a list of all workspaces.
     * @return List of all workspaces
     */
    public List<Workspace> getListOfAllWorkSpaces() {
        return workspaceRepository.findAll();
    }

    /**
     * Creates a new workspace with the given name.
     * @param workspace Workspace request object containing the name
     * @return The created workspace object
     * @throws WorkspaceAlreadyExistException if a workspace with the same name already exists
     */
    @Auditable(actionType = ActionType.CREATE_WORKSPACE)
    public Workspace createWorkspace(WorkspaceRequest workspace) throws WorkspaceAlreadyExistException {
        ValidationUtil.validate(workspace);
        Workspace newWorkspace = Workspace.builder().name(workspace.getName()).build();
        return workspaceRepository.save(newWorkspace);
    }

    /**
     * Deletes a workspace by its name.
     * @param name Name of the workspace to delete
     * @return True if deletion was successful, false otherwise
     * @throws WorkspaceNotFoundException if the workspace with the specified name does not exist
     */
    @Auditable(actionType = ActionType.DELETE_WORKSPACE)
    public boolean deleteWorkspace(String name) throws WorkspaceNotFoundException{
        Workspace workspace = this.getWorkspace(name);
        return workspaceRepository.deleteByName(workspace.getName());
    }

    /**
     * Deletes a workspace by its ID.
     * @param id ID of the workspace to delete
     * @return True if deletion was successful, false otherwise
     * @throws WorkspaceNotFoundException if the workspace with the specified ID does not exist
     */
    @Auditable(actionType = ActionType.DELETE_WORKSPACE)
    public boolean deleteWorkspace(Long id) {
        Workspace workspace = this.getWorkspace(id);
        return workspaceRepository.deleteById(workspace.getId());
    }

    /**
     * Updates the name of a workspace.
     * @param oldName Current name of the workspace
     * @param workspaceRequest Updated workspace object with the new name
     * @return True if update was successful, false otherwise
     * @throws WorkspaceNotFoundException if the workspace with the specified old name does not exist
     * @throws WorkspaceAlreadyExistException if a workspace with the updated name already exists
     */
    @Auditable(actionType = ActionType.UPDATE_WORKSPACE)
    public boolean updateWorkspace(String oldName, WorkspaceRequest workspaceRequest) throws WorkspaceNotFoundException, WorkspaceAlreadyExistException {
        ValidationUtil.validate(workspaceRequest);

        Workspace oldWorkspace = this.getWorkspace(oldName);

        Optional<Workspace> w = workspaceRepository.findByName(workspaceRequest.getName());
        if (w.isPresent()) throw new WorkspaceAlreadyExistException("Workspace with this name already exists.");

        Workspace updatedWorkspace = Workspace.builder()
                                              .id(oldWorkspace.getId())
                                              .name(workspaceRequest.getName()).build();
        return workspaceRepository.update(updatedWorkspace);
    }

    /**
     * Retrieves a workspace by its name.
     * @param name Name of the workspace to retrieve
     * @return Workspace object if found
     * @throws WorkspaceNotFoundException if the workspace with the specified name does not exist
     */
    public Workspace getWorkspace(String name) throws WorkspaceNotFoundException{
        return workspaceRepository.findByName(name)
                .orElseThrow(() -> new WorkspaceNotFoundException("Workspace with this name doesn't exists."));
    }

    /**
     * Retrieves a workspace by its ID.
     * @param id ID of the workspace to retrieve
     * @return Workspace object if found
     * @throws WorkspaceNotFoundException if the workspace with the specified ID does not exist
     */
    public Workspace getWorkspace(Long id) throws WorkspaceNotFoundException{
        return workspaceRepository.findById(id)
                .orElseThrow(() -> new WorkspaceNotFoundException("Workspace with this name doesn't exists."));
    }

}
