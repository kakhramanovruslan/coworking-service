package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Represents a request object for authentication, containing username and password.
 */
public record AuthRequest(
        /**
         * The username for authentication.
         */
        @NotNull(message = "Username cannot be null")
        @NotBlank(message = "Username cannot be blank")
        @Size(min = 3, max = 50, message = "Username length must be between 3 and 50 characters")
        String username,

        /**
         * The password for authentication.
         */
        @NotNull(message = "Password cannot be null")
        @NotBlank(message = "Password cannot be blank")
        @Size(min = 5, max = 30, message = "Password length must be between 5 and 30 characters")
        String password) {
}
