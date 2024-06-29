package org.example.service;

import org.example.entity.Booking;
import org.example.entity.Workspace;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CoworkingServiceTest {

    @InjectMocks
    private CoworkingService coworkingService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        coworkingService = new CoworkingService();
    }

    @Test
    public void testCreateWorkspace() {
        setUp();
        boolean result = coworkingService.createWorkspace("TestWorkspace");
        assertThat(result).isTrue();
        List<Workspace> workspaces = coworkingService.getListOfAllWorkSpaces();
        assertThat(workspaces).hasSize(1);
        assertThat(workspaces.get(0).getName()).isEqualTo("TestWorkspace");
    }

    @Test
    public void testDeleteWorkspaceByName() {
        setUp();
        coworkingService.createWorkspace("TestWorkspace");
        boolean result = coworkingService.deleteWorkspace("TestWorkspace");
        assertThat(result).isTrue();
        List<Workspace> workspaces = coworkingService.getListOfAllWorkSpaces();
        assertThat(workspaces).isEmpty();
    }

    @Test
    public void testUpdateWorkspace() {
        setUp();
        coworkingService.createWorkspace("OldName");
        Workspace workspace = coworkingService.getWorkspace("OldName");
        boolean result = coworkingService.updateWorkspace(workspace.getId(), "NewName");
        assertThat(result).isTrue();
        workspace = coworkingService.getWorkspace(workspace.getId());
        assertThat(workspace.getName()).isEqualTo("NewName");
    }

    @Test
    public void testBookWorkspace() {
        setUp();
        coworkingService.createWorkspace("TestWorkspace");
        LocalDateTime startTime = LocalDateTime.now().plusHours(1);
        LocalDateTime endTime = startTime.plusHours(1);
        boolean result = coworkingService.bookWorkspace("TestWorkspace", startTime, endTime, "testUser");
        assertThat(result).isTrue();
        List<Booking> bookings = coworkingService.filterBookingsByUser("testUser");
        assertThat(bookings).hasSize(1);
        assertThat(bookings.get(0).getWorkspaceName()).isEqualTo("TestWorkspace");
    }

    @Test
    public void testFilterBookingsByUser() {
        setUp();
        coworkingService.createWorkspace("TestWorkspace");
        LocalDateTime startTime = LocalDateTime.now().plusHours(1);
        LocalDateTime endTime = startTime.plusHours(1);
        coworkingService.bookWorkspace("TestWorkspace", startTime, endTime, "testUser");
        List<Booking> bookings = coworkingService.filterBookingsByUser("testUser");
        assertThat(bookings).hasSize(1);
        assertThat(bookings.get(0).getWorkspaceName()).isEqualTo("TestWorkspace");
    }
}