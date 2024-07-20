package org.example.controllers;

import jakarta.servlet.ServletContext;
import lombok.RequiredArgsConstructor;
import org.example.dto.*;
import org.example.entity.Audit;
import org.example.entity.Workspace;
import org.example.entity.types.Role;
import org.example.service.AuditService;
import org.example.service.WorkspaceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

/**
 * Controller for managing admin operations.
 */
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AuditService auditService;
    private final WorkspaceService workspaceService;
    private final ServletContext servletContext;

    @PostMapping("/workspaces")
    public ResponseEntity<Workspace> createWorkspace(@RequestBody WorkspaceRequest request) throws AccessDeniedException {
        isAdmin();
        return ResponseEntity.ok(workspaceService.createWorkspace(request));
    }

    @PutMapping("/workspaces")
    public ResponseEntity<Void> updateWorkspace(@RequestParam String name, @RequestBody WorkspaceRequest workspace) throws AccessDeniedException {
        isAdmin();
        workspaceService.updateWorkspace(name, workspace);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/workspaces/name/{name}")
    public ResponseEntity<Void> deleteWorkspaceByName(@PathVariable("name") String name) throws AccessDeniedException {
        isAdmin();
        workspaceService.deleteWorkspace(name);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/workspaces/id/{id}")
    public ResponseEntity<Void> deleteWorkspaceById(@PathVariable("id") Long id) throws AccessDeniedException {
        isAdmin();
        workspaceService.deleteWorkspace(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/audits")
    public ResponseEntity<List<Audit>> getListOfAllAudits() throws AccessDeniedException {
        isAdmin();
        return ResponseEntity.ok(auditService.getAllAudits());
    }

    private void isAdmin() throws AccessDeniedException {
        Authentication authentication = (Authentication) servletContext.getAttribute("authentication");
        if (authentication.getRole() != Role.ADMIN) {
            throw new AccessDeniedException("You do not have permission to access this page.");
        }
    }

}
