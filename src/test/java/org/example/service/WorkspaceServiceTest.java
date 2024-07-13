package org.example.service;

import org.example.repository.WorkspaceRepository;
import org.example.dto.WorkspaceRequest;
import org.example.entity.Workspace;
import org.example.exceptions.WorkspaceAlreadyExistException;
import org.example.exceptions.WorkspaceNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WorkspaceServiceTest {

    @Mock
    private WorkspaceRepository workspaceRepository;

    @InjectMocks
    private WorkspaceService workspaceService;

    @Test
    @DisplayName("Test creating a new workspace")
    void testCreateWorkspace() throws WorkspaceAlreadyExistException {
        String workspaceName = "New Workspace";
        WorkspaceRequest workspaceRequest = buildWorkspaceRequest(workspaceName);

        Workspace newWorkspace = Workspace.builder().name(workspaceName).build();

        when(workspaceRepository.save(any(Workspace.class))).thenReturn(newWorkspace);

        Workspace createdWorkspace = workspaceService.createWorkspace(workspaceRequest);

        assertNotNull(createdWorkspace);
        assertEquals(workspaceName, createdWorkspace.getName());
        verify(workspaceRepository, times(1)).save(any(Workspace.class));
    }

    @Test
    @DisplayName("Test create workspace throws WorkspaceAlreadyExistException")
    void testCreateWorkspaceThrowsException() {
        String workspaceName = "Existing Workspace";
        WorkspaceRequest workspaceRequest = new WorkspaceRequest(workspaceName);

        when(workspaceRepository.save(any(Workspace.class)))
                .thenThrow(new WorkspaceAlreadyExistException("Workspace with name '" + workspaceName + "' already exists"));

        assertThrows(WorkspaceAlreadyExistException.class, () -> workspaceService.createWorkspace(workspaceRequest));
        verify(workspaceRepository, times(1)).save(any(Workspace.class));
    }

    @Test
    @DisplayName("Test deleting a workspace by name")
    void testDeleteWorkspaceByName() throws WorkspaceNotFoundException {
        String workspaceName = "Test Workspace";
        Workspace workspace = new Workspace();
        workspace.setName(workspaceName);

        when(workspaceRepository.findByName(workspaceName)).thenReturn(Optional.of(workspace));
        when(workspaceRepository.deleteByName(workspaceName)).thenReturn(true);

        boolean result = workspaceService.deleteWorkspace(workspaceName);

        assertTrue(result);
        verify(workspaceRepository, times(1)).findByName(workspaceName);
        verify(workspaceRepository, times(1)).deleteByName(workspaceName);
    }

    @Test
    @DisplayName("Test: Update workspace name")
    void testUpdateWorkspaceName() throws WorkspaceNotFoundException, WorkspaceAlreadyExistException {
        String oldName = "Old Workspace";
        String newName = "Updated Workspace";

        Workspace existingWorkspace = Workspace.builder().name(oldName).build();
        when(workspaceRepository.findByName(oldName)).thenReturn(Optional.of(existingWorkspace));

        when(workspaceRepository.findByName(newName)).thenReturn(Optional.empty());
        when(workspaceRepository.update(any(Workspace.class))).thenReturn(true);

        WorkspaceRequest updatedWorkspace = WorkspaceRequest.builder().name(newName).build();
        boolean result = workspaceService.updateWorkspace(oldName, updatedWorkspace);

        assertTrue(result);
        verify(workspaceRepository, times(1)).findByName(oldName);
        verify(workspaceRepository, times(1)).findByName(newName);
    }

    public WorkspaceRequest buildWorkspaceRequest(String name){
        return WorkspaceRequest.builder()
                .name(name)
                .build();
    }
}
