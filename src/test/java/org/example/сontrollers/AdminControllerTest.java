package org.example.сontrollers;

import jakarta.servlet.ServletContext;
import org.example.controllers.AdminController;
import org.example.dto.Authentication;
import org.example.dto.WorkspaceRequest;
import org.example.entity.Audit;
import org.example.entity.Workspace;
import org.example.entity.types.ActionType;
import org.example.entity.types.Role;
import org.example.exceptions.InvalidCredentialsException;
import org.example.exceptions.WorkspaceAlreadyExistException;
import org.example.service.AuditService;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AdminControllerTest {

    @Mock
    private AuditService auditService;

    @Mock
    private WorkspaceService workspaceService;

    @Mock
    private ServletContext servletContext;

    @InjectMocks
    private AdminController adminController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(adminController).build();
    }

    @Test
    @DisplayName("Test creating workspace success")
    public void testCreateWorkspace() throws Exception {
        WorkspaceRequest request = new WorkspaceRequest("Workspace1");
        Workspace workspace = Workspace.builder().name("Workspace1").build();

        when(servletContext.getAttribute("authentication")).thenReturn(new Authentication("admin", Role.ADMIN));
        when(workspaceService.createWorkspace(request)).thenReturn(workspace);

        mockMvc.perform(post("/admin/workspaces")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"name\": \"Workspace1\" }"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Test creating workspace fail")
    public void testCreateWorkspaceFail() {
        WorkspaceRequest request = new WorkspaceRequest("Workspace1");

        when(servletContext.getAttribute("authentication")).thenReturn(new Authentication("admin", Role.ADMIN));
        when(workspaceService.createWorkspace(request)).thenThrow(new WorkspaceAlreadyExistException("Workspace already exist."));

        WorkspaceAlreadyExistException exception = assertThrows(WorkspaceAlreadyExistException.class, () -> {
            adminController.createWorkspace(request);
        });

        assertEquals("Workspace already exist.", exception.getMessage());
    }

    @Test
    @DisplayName("Test updating workspace success")
    public void testUpdateWorkspace() throws Exception {
        String name = "Workspace1";

        when(servletContext.getAttribute("authentication")).thenReturn(new Authentication("admin", Role.ADMIN));

        mockMvc.perform(put("/admin/workspaces")
                        .param("name", name)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"name\": \"UpdatedWorkspace\" }"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Test deleting workspace by name success")
    public void testDeleteWorkspaceByName() throws Exception {
        String name = "Workspace1";

        when(servletContext.getAttribute("authentication")).thenReturn(new Authentication("admin", Role.ADMIN));

        mockMvc.perform(delete("/admin/workspaces/name/{name}", name)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Test deleting workspace by id success")
    public void testDeleteWorkspaceById() throws Exception {
        Long id = 1L;

        when(servletContext.getAttribute("authentication")).thenReturn(new Authentication("admin", Role.ADMIN));

        mockMvc.perform(delete("/admin/workspaces/id/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Test getting list of all audits success")
    public void testGetListOfAllAudits() throws Exception {
        List<Audit> audits = Arrays.asList((Audit.builder().actionType(ActionType.AUTHORIZATION).build()), Audit.builder().actionType(ActionType.REGISTRATION).build());

        when(servletContext.getAttribute("authentication")).thenReturn(new Authentication("admin", Role.ADMIN));
        when(auditService.getAllAudits()).thenReturn(audits);

        mockMvc.perform(get("/admin/audits")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(servletContext, times(1)).getAttribute("authentication");
        verify(auditService, times(1)).getAllAudits();
    }

}
