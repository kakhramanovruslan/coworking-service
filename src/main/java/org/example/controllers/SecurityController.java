package org.example.controllers;

import jakarta.servlet.ServletContext;
import lombok.RequiredArgsConstructor;
import org.example.annotations.Loggable;
import org.example.dto.AuthRequest;
import org.example.dto.TokenResponse;
import org.example.dto.UserDTO;
import org.example.service.SecurityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * Controller class for handling authentication and authorization operations.
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class SecurityController {

    private final SecurityService securityService;
    private final ServletContext servletContext;

    @PostMapping("/registration")
    public ResponseEntity<UserDTO> register(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(securityService.register(request));
    }

//    @Loggable
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> authenticate(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(securityService.authenticate(request));
    }
}