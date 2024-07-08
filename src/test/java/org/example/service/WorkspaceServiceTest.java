package org.example.service;

import org.example.dao.WorkspaceDao;
import org.example.dto.WorkspaceRequest;
import org.example.entity.Workspace;
import org.example.exceptions.WorkspaceAlreadyExistException;
import org.example.exceptions.WorkspaceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WorkspaceServiceTest {

    @Mock
    private WorkspaceDao workspaceDao;

    @InjectMocks
    private WorkspaceService workspaceService;

    @Test
    @DisplayName("Test creating a new workspace")
    void testCreateWorkspace() throws WorkspaceAlreadyExistException {
        String workspaceName = "New Workspace";
        WorkspaceRequest workspaceRequest = buildWorkspaceRequest(workspaceName);

        Workspace newWorkspace = Workspace.builder().name(workspaceName).build();

        when(workspaceDao.findByName(workspaceName)).thenReturn(Optional.empty());
        when(workspaceDao.save(any(Workspace.class))).thenReturn(newWorkspace);

        Workspace createdWorkspace = workspaceService.createWorkspace(workspaceRequest);

        assertNotNull(createdWorkspace);
        assertEquals(workspaceName, createdWorkspace.getName());
        verify(workspaceDao, times(1)).findByName(workspaceName);
        verify(workspaceDao, times(1)).save(any(Workspace.class));
    }

    @Test
    @DisplayName("Test: Create a new workspace - WorkspaceAlreadyExistException")
    void testCreateWorkspaceAlreadyExists() {
        String existingWorkspaceName = "Existing Workspace";
        WorkspaceRequest workspaceRequest = buildWorkspaceRequest(existingWorkspaceName);

        when(workspaceDao.findByName(existingWorkspaceName)).thenReturn(Optional.of(new Workspace()));

        assertThrows(WorkspaceAlreadyExistException.class, () -> {
            workspaceService.createWorkspace(workspaceRequest);
        });

        verify(workspaceDao, times(1)).findByName(existingWorkspaceName);
        verify(workspaceDao, never()).save(any(Workspace.class));
    }

    @Test
    @DisplayName("Test deleting a workspace by name")
    void testDeleteWorkspaceByName() throws WorkspaceNotFoundException {
        String workspaceName = "Test Workspace";
        Workspace workspace = new Workspace();
        workspace.setName(workspaceName);

        when(workspaceDao.findByName(workspaceName)).thenReturn(Optional.of(workspace));
        when(workspaceDao.deleteByName(workspaceName)).thenReturn(true);

        boolean result = workspaceService.deleteWorkspace(workspaceName);

        assertTrue(result);
        verify(workspaceDao, times(1)).findByName(workspaceName);
        verify(workspaceDao, times(1)).deleteByName(workspaceName);
    }

    @Test
    @DisplayName("Test: Update workspace name")
    void testUpdateWorkspaceName() throws WorkspaceNotFoundException, WorkspaceAlreadyExistException {
        String oldName = "Old Workspace";
        String newName = "Updated Workspace";

        Workspace existingWorkspace = Workspace.builder().name(oldName).build();
        when(workspaceDao.findByName(oldName)).thenReturn(Optional.of(existingWorkspace));

        when(workspaceDao.findByName(newName)).thenReturn(Optional.empty());
        when(workspaceDao.update(any(Workspace.class))).thenReturn(true);

        Workspace updatedWorkspace = Workspace.builder().name(newName).build();
        boolean result = workspaceService.updateWorkspace(oldName, updatedWorkspace);

        assertTrue(result);
        verify(workspaceDao, times(1)).findByName(oldName);
        verify(workspaceDao, times(1)).findByName(newName);
        verify(workspaceDao, times(1)).update(updatedWorkspace);
    }

    public WorkspaceRequest buildWorkspaceRequest(String name){
        return WorkspaceRequest.builder()
                .name(name)
                .build();
    }
}
