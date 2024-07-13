package org.example.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.example.annotations.Loggable;
import org.example.dto.AuthRequest;
import org.example.dto.TokenResponse;
import org.example.dto.UserDTO;
import org.example.service.SecurityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Controller class for handling authentication and authorization operations.
 */
@Api(value = "Security Controller")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class SecurityController {

    private final SecurityService securityService;
    private final ServletContext servletContext;

    @ApiOperation(value = "Register a new user", response = UserDTO.class)
    @PostMapping("/registration")
    public ResponseEntity<UserDTO> register(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(securityService.register(request));
    }

    @Loggable
    @ApiOperation(value = "Authenticate user", response = TokenResponse.class)
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> authenticate(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(securityService.authenticate(request.username(), request.password()));
    }

    @ApiOperation(value = "Logout the current user", response = ResponseEntity.class)
    @GetMapping("/logout")
    public ResponseEntity logout() {
        securityService.logout(servletContext);
        return ResponseEntity.ok().build();
    }
}