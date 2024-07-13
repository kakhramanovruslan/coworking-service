package org.example.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.example.dto.*;
import org.example.entity.Audit;
import org.example.entity.Workspace;
import org.example.entity.types.Role;
import org.example.service.AuditService;
import org.example.service.WorkspaceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;
import java.nio.file.AccessDeniedException;
import java.util.List;


@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Api(value = "Application administration")
public class AdminController {

    private final AuditService auditService;
    private final WorkspaceService workspaceService;
    private final ServletContext servletContext;

    @ApiImplicitParam(name = "Authorization", value = "Bearer token", required = true, dataTypeClass = String.class, paramType = "header")
    @ApiOperation(value = "Create a new workspace", response = Workspace.class)
    @PostMapping("/workspaces")
    public ResponseEntity<Workspace> createWorkspace(@RequestBody WorkspaceRequest request) throws AccessDeniedException {
        isAdmin();
        return ResponseEntity.ok(workspaceService.createWorkspace(request));
    }

    @ApiImplicitParam(name = "Authorization", value = "Bearer token", required = true, dataTypeClass = String.class, paramType = "header")
    @ApiOperation(value = "Update workspace")
    @PutMapping("/workspaces")
    public ResponseEntity<Void> updateWorkspace(@RequestParam String name, @RequestBody WorkspaceRequest workspace) throws AccessDeniedException {
        isAdmin();
        workspaceService.updateWorkspace(name, workspace);
        return ResponseEntity.ok().build();
    }

    @ApiImplicitParam(name = "Authorization", value = "Bearer token", required = true, dataTypeClass = String.class, paramType = "header")
    @ApiOperation(value = "Delete workspace")
    @DeleteMapping("/workspaces")
    public ResponseEntity<Void> deleteWorkspace(@RequestParam String name) throws AccessDeniedException {
        isAdmin();
        workspaceService.deleteWorkspace(name);
        return ResponseEntity.ok().build();
    }

    @ApiImplicitParam(name = "Authorization", value = "Bearer token", required = true, dataTypeClass = String.class, paramType = "header")
    @ApiOperation(value = "Get list of all audits", response = Audit.class)
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
