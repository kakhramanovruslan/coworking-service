package org.example.service;
import org.example.dao.impl.WorkspaceDaoImpl;
import org.example.entity.Workspace;
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
    private WorkspaceDaoImpl workspaceDao;

    @InjectMocks
    private WorkspaceService workspaceService;

    @Test
    void testGetListOfAllWorkspaces() {
        when(workspaceDao.findAll()).thenReturn(List.of());

        List<Workspace> result = workspaceService.getListOfAllWorkSpaces();

        assertTrue(result.isEmpty());
    }

    @Test
    void testCreateWorkspace() {
        String name = "new workspace";
        Workspace newWorkspace = Workspace.builder().name(name).build();
        when(workspaceDao.save(any(Workspace.class))).thenReturn(newWorkspace);

        Workspace result = workspaceService.createWorkspace(name);

        assertEquals(newWorkspace, result);
        verify(workspaceDao).save(any(Workspace.class));
    }

    @Test
    void testDeleteWorkspaceByNameIfWorkspaceExists() {
        String name = "existing workspace";
        when(workspaceDao.deleteByName(name)).thenReturn(true);

        boolean result = workspaceService.deleteWorkspace(name);

        assertTrue(result);
        verify(workspaceDao).deleteByName(name);
    }

    @Test
    void testDeleteWorkspaceByNameIfWorkspaceDoesNotExist() {
        String name = "non existing workspace";
        when(workspaceDao.deleteByName(name)).thenReturn(false);

        boolean result = workspaceService.deleteWorkspace(name);

        assertFalse(result);
        verify(workspaceDao).deleteByName(name);
    }

    @Test
    void testDeleteWorkspaceByIdIfWorkspaceExists() {
        Long id = 1L;
        when(workspaceDao.deleteById(id)).thenReturn(true);

        boolean result = workspaceService.deleteWorkspace(id);

        assertTrue(result);
        verify(workspaceDao).deleteById(id);
    }

    @Test
    void testDeleteWorkspaceByIdIfWorkspaceDoesNotExist() {
        Long id = 1L;
        when(workspaceDao.deleteById(id)).thenReturn(false);

        boolean result = workspaceService.deleteWorkspace(id);

        assertFalse(result);
        verify(workspaceDao).deleteById(id);
    }

    @Test
    void testUpdateWorkspaceIfWorkspaceExists() {
        String oldName = "old name";
        String newName = "new name";
        Workspace oldWorkspace = Workspace.builder().name(oldName).build();
        when(workspaceDao.findByName(oldName)).thenReturn(Optional.of(oldWorkspace));
        when(workspaceDao.update(any(Workspace.class))).thenReturn(true);

        boolean result = workspaceService.updateWorkspace(oldName, newName);

        assertTrue(result);
        verify(workspaceDao).update(any(Workspace.class));
    }

    @Test
    void testUpdateWorkspaceIfWorkspaceDoesNotExist() {
        String oldName = "non existing workspace";
        String newName = "new name";

        boolean result = workspaceService.updateWorkspace(oldName, newName);

        assertFalse(result);
        verify(workspaceDao, never()).update(any(Workspace.class));
    }

    @Test
    void testGetWorkspaceByNameIfWorkspaceExists() {
        String name = "existing workspace";
        Workspace workspace = Workspace.builder().name(name).build();
        when(workspaceDao.findByName(name)).thenReturn(Optional.of(workspace));

        Optional<Workspace> result = workspaceService.getWorkspace(name);

        assertTrue(result.isPresent());
        assertEquals(workspace, result.get());
    }

    @Test
    void testGetWorkspaceByNameIfWorkspaceDoesNotExist() {
        String name = "non existing workspace";

        Optional<Workspace> result = workspaceService.getWorkspace(name);

        assertTrue(result.isEmpty());
    }

    @Test
    void testGetWorkspaceByIdIfWorkspaceExists() {
        Long id = 1L;
        Workspace workspace = Workspace.builder().id(id).build();
        when(workspaceDao.findById(id)).thenReturn(Optional.of(workspace));

        Optional<Workspace> result = workspaceService.getWorkspace(id);

        assertTrue(result.isPresent());
        assertEquals(workspace, result.get());
    }

    @Test
    void testGetWorkspaceByIdIfWorkspaceDoesNotExist() {
        Long id = 1L;

        Optional<Workspace> result = workspaceService.getWorkspace(id);

        assertTrue(result.isEmpty());
    }
}