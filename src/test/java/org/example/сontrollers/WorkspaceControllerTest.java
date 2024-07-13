package org.example.сontrollers;

import org.example.controllers.WorkspaceController;
import org.example.entity.Workspace;
import org.example.service.BookingService;
import org.example.service.WorkspaceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class WorkspaceControllerTest {

    @Mock
    private WorkspaceService workspaceService;

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private WorkspaceController workspaceController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(workspaceController).build();
    }

    @Test
    @DisplayName("Test getting all workspaces success")
    public void testGetListOfAllWorkspaces() throws Exception {
        List<Workspace> workspaces = Arrays.asList(
                Workspace.builder().name("Workspace1").build(), Workspace.builder().name("Workspace2").build()
        );

        when(workspaceService.getListOfAllWorkSpaces()).thenReturn(workspaces);

        mockMvc.perform(get("/workspaces/list")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(workspaceService, times(1)).getListOfAllWorkSpaces();
    }

    @Test
    @DisplayName("Test getting workspace by name success")
    public void testGetWorkspaceByName() throws Exception {
        String name = "Workspace1";
        Workspace workspace = Workspace.builder().name(name).build();

        when(workspaceService.getWorkspace(name)).thenReturn(workspace);

        mockMvc.perform(get("/workspaces/name/{name}", name)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(workspaceService, times(1)).getWorkspace(name);
    }

    @Test
    @DisplayName("Test getting workspace by id success")
    public void testGetWorkspaceById() throws Exception {
        String id = "1";
        Workspace workspace = Workspace.builder().name("Workspace1").build();
        workspace.setId(Long.parseLong(id));

        when(workspaceService.getWorkspace(Long.parseLong(id))).thenReturn(workspace);

        mockMvc.perform(get("/workspaces/id/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(workspaceService, times(1)).getWorkspace(Long.parseLong(id));
    }

    @Test
    @DisplayName("Test getting available workspaces at now success")
    public void testGetAvailableWorkspacesAtNow() throws Exception {
        List<Workspace> availableWorkspaces = Arrays.asList(
                Workspace.builder().name("Workspace1").build(), Workspace.builder().name("Workspace2").build()
        );
        when(bookingService.getAvailableWorkspacesAtNow()).thenReturn(availableWorkspaces);

        mockMvc.perform(get("/workspaces/available")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(bookingService, times(1)).getAvailableWorkspacesAtNow();
    }

    @Test
    @DisplayName("Test getting available workspaces for time period success")
    public void testGetAvailableWorkspacesForTimePeriod() throws Exception {
        String startTime = "2024-07-15T09:00:00";
        String endTime = "2024-07-15T18:00:00";

        List<Workspace> availableWorkspaces = Arrays.asList(
                Workspace.builder().name("Workspace1").build(), Workspace.builder().name("Workspace2").build()
        );

        when(bookingService.getAvailableWorkspacesForTimePeriod(startTime, endTime)).thenReturn(availableWorkspaces);

        mockMvc.perform(get("/workspaces/available-period")
                        .param("startTime", startTime)
                        .param("endTime", endTime)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(bookingService, times(1)).getAvailableWorkspacesForTimePeriod(startTime, endTime);
    }
}
