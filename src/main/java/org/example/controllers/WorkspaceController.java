package org.example.controllers;

import lombok.RequiredArgsConstructor;
import org.example.entity.Workspace;
import org.example.service.BookingService;
import org.example.service.WorkspaceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

/**
 * Controller for managing workspaces.
 */
@RestController
@RequestMapping("/workspaces")
@RequiredArgsConstructor
public class WorkspaceController {

    private final WorkspaceService workspaceService;
    private final BookingService bookingService;

    @GetMapping("/list")
    public ResponseEntity<List<Workspace>> getListOfAllWorkspaces() throws AccessDeniedException {
        return ResponseEntity.ok(workspaceService.getListOfAllWorkSpaces());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Workspace> getWorkspaceByName(@PathVariable("name") String name) throws AccessDeniedException {
        Workspace workspaces = workspaceService.getWorkspace(name);
        return ResponseEntity.ok(workspaces);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Workspace> getWorkspaceById(@PathVariable("id") Long id) throws AccessDeniedException {
        return ResponseEntity.ok(workspaceService.getWorkspace(id));
    }

    @GetMapping("/available")
    public ResponseEntity<List<Workspace>> getAvailableWorkspacesAtNow() throws AccessDeniedException {
        return ResponseEntity.ok(bookingService.getAvailableWorkspacesAtNow());
    }

    @GetMapping("/available-period")
    public ResponseEntity<List<Workspace>> getAvailableWorkspacesForTimePeriod(@RequestParam String startTime, @RequestParam String endTime) throws AccessDeniedException {
        return ResponseEntity.ok(bookingService.getAvailableWorkspacesForTimePeriod(startTime, endTime));
    }
}
