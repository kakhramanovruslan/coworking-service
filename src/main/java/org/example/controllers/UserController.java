package org.example.controllers;

import jakarta.servlet.ServletContext;
import lombok.RequiredArgsConstructor;
import org.example.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ServletContext servletContext;

    @GetMapping("/logout")
    public ResponseEntity<Void> logout() {
        userService.logout(servletContext);
        return ResponseEntity.ok().build();
    }
}