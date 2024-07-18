package org.example.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.example.entity.Audit;
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
@Api(value = "Workspace Controller")
public class WorkspaceController {

    private final WorkspaceService workspaceService;
    private final BookingService bookingService;

    @ApiImplicitParam(name = "Authorization", value = "Bearer token", required = true, dataTypeClass = String.class, paramType = "header")
    @ApiOperation(value = "Get list of all workspaces", response = Audit.class)
    @GetMapping("/list")
    public ResponseEntity<List<Workspace>> getListOfAllWorkspaces() throws AccessDeniedException {
        return ResponseEntity.ok(workspaceService.getListOfAllWorkSpaces());
    }

    @ApiImplicitParam(name = "Authorization", value = "Bearer token", required = true, dataTypeClass = String.class, paramType = "header")
    @ApiOperation(value = "Get workspace by name", response = Workspace.class)
    @GetMapping("/name/{name}")
    public ResponseEntity<Workspace> getWorkspaceByName(@PathVariable("name") String name) throws AccessDeniedException {
        Workspace workspaces = workspaceService.getWorkspace(name);
        return ResponseEntity.ok(workspaces);
    }

    @ApiImplicitParam(name = "Authorization", value = "Bearer token", required = true, dataTypeClass = String.class, paramType = "header")
    @ApiOperation(value = "Get workspace by id", response = Workspace.class)
    @GetMapping("/id/{id}")
    public ResponseEntity<Workspace> getWorkspaceById(@PathVariable("id") Long id) throws AccessDeniedException {
        return ResponseEntity.ok(workspaceService.getWorkspace(id));
    }

    @ApiImplicitParam(name = "Authorization", value = "Bearer token", required = true, dataTypeClass = String.class, paramType = "header")
    @ApiOperation(value = "Get list of all available workspaces at the current time", response = Audit.class)
    @GetMapping("/available")
    public ResponseEntity<List<Workspace>> getAvailableWorkspacesAtNow() throws AccessDeniedException {
        return ResponseEntity.ok(bookingService.getAvailableWorkspacesAtNow());
    }

    @ApiImplicitParam(name = "Authorization", value = "Bearer token", required = true, dataTypeClass = String.class, paramType = "header")
    @ApiOperation(value = "Get list of all available workspaces at the current time", response = Audit.class)
    @GetMapping("/available-period")
    public ResponseEntity<List<Workspace>> getAvailableWorkspacesForTimePeriod(@RequestParam String startTime, @RequestParam String endTime) throws AccessDeniedException {
        return ResponseEntity.ok(bookingService.getAvailableWorkspacesForTimePeriod(startTime, endTime));
    }
}
