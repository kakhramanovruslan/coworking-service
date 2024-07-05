package org.example.service;

import org.example.dao.WorkspaceDao;
import org.example.entity.Workspace;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkspaceServiceTest {

    @Mock
    private WorkspaceDao workspaceDao;

    @InjectMocks
    private WorkspaceService workspaceService;

    @Test
    @DisplayName("Test getting list of all workspaces")
    void testGetListOfAllWorkspaces() {
        when(workspaceDao.findAll()).thenReturn(List.of());

        List<Workspace> result = workspaceService.getListOfAllWorkSpaces();

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Test creating a workspace")
    void testCreateWorkspace() {
        String name = "new workspace";
        Workspace newWorkspace = Workspace.builder().name(name).build();
        when(workspaceDao.save(any(Workspace.class))).thenReturn(newWorkspace);

        Workspace result = workspaceService.createWorkspace(newWorkspace);

        assertEquals(newWorkspace, result);
        verify(workspaceDao).save(any(Workspace.class));
    }

    @Test
    @DisplayName("Test deleting workspace by name if it exists")
    void testDeleteWorkspaceByNameIfWorkspaceExists() {
        String name = "existing workspace";
        when(workspaceDao.deleteByName(name)).thenReturn(true);

        boolean result = workspaceService.deleteWorkspace(name);

        assertTrue(result);
        verify(workspaceDao).deleteByName(name);
    }

    @Test
    @DisplayName("Test deleting workspace by name if it does not exist")
    void testDeleteWorkspaceByNameIfWorkspaceDoesNotExist() {
        String name = "non existing workspace";
        when(workspaceDao.deleteByName(name)).thenReturn(false);

        boolean result = workspaceService.deleteWorkspace(name);

        assertFalse(result);
        verify(workspaceDao).deleteByName(name);
    }

    @Test
    @DisplayName("Test deleting workspace by ID if it exists")
    void testDeleteWorkspaceByIdIfWorkspaceExists() {
        Long id = 1L;
        when(workspaceDao.deleteById(id)).thenReturn(true);

        boolean result = workspaceService.deleteWorkspace(id);

        assertTrue(result);
        verify(workspaceDao).deleteById(id);
    }

    @Test
    @DisplayName("Test deleting workspace by ID if it does not exist")
    void testDeleteWorkspaceByIdIfWorkspaceDoesNotExist() {
        Long id = 1L;
        when(workspaceDao.deleteById(id)).thenReturn(false);

        boolean result = workspaceService.deleteWorkspace(id);

        assertFalse(result);
        verify(workspaceDao).deleteById(id);
    }

    @Test
    @DisplayName("Test updating workspace if it exists")
    void testUpdateWorkspaceIfWorkspaceExists() {
        String oldName = "old name";
        Workspace oldWorkspace = Workspace.builder().name(oldName).build();
        when(workspaceDao.findByName(oldName)).thenReturn(Optional.of(oldWorkspace));
        when(workspaceDao.update(any(Workspace.class))).thenReturn(true);

        boolean result = workspaceService.updateWorkspace(oldName, Workspace.builder().build());

        assertTrue(result);
        verify(workspaceDao).update(any(Workspace.class));
    }

    @Test
    @DisplayName("Test updating workspace if it does not exist")
    void testUpdateWorkspaceIfWorkspaceDoesNotExist() {
        String oldName = "non existing workspace";

        boolean result = workspaceService.updateWorkspace(oldName, any(Workspace.class));

        assertFalse(result);
        verify(workspaceDao, never()).update(any(Workspace.class));
    }

    @Test
    @DisplayName("Test getting workspace by name if it exists")
    void testGetWorkspaceByNameIfWorkspaceExists() {
        String name = "existing workspace";
        Workspace workspace = Workspace.builder().name(name).build();
        when(workspaceDao.findByName(name)).thenReturn(Optional.of(workspace));

        Optional<Workspace> result = workspaceService.getWorkspaceByName(name);

        assertTrue(result.isPresent());
        assertEquals(workspace, result.get());
    }

    @Test
    @DisplayName("Test getting workspace by name if it does not exist")
    void testGetWorkspaceByNameIfWorkspaceDoesNotExist() {
        String name = "non existing workspace";

        Optional<Workspace> result = workspaceService.getWorkspaceByName(name);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Test getting workspace by ID if it exists")
    void testGetWorkspaceByIdIfWorkspaceExists() {
        Long id = 1L;
        Workspace workspace = Workspace.builder().id(id).build();
        when(workspaceDao.findById(id)).thenReturn(Optional.of(workspace));

        Optional<Workspace> result = workspaceService.getWorkspaceById(id);

        assertTrue(result.isPresent());
        assertEquals(workspace, result.get());
    }

    @Test
    @DisplayName("Test getting workspace by ID if it does not exist")
    void testGetWorkspaceByIdIfWorkspaceDoesNotExist() {
        Long id = 1L;

        Optional<Workspace> result = workspaceService.getWorkspaceById(id);

        assertTrue(result.isEmpty());
    }
}
