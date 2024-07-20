package org.example.dto;

import lombok.*;
import org.example.entity.types.Role;

/**
 * Data Transfer Object which represent a user.
 */
@Getter
@Setter
@Builder
public class UserDTO {
    /**
     * The unique identifier of the user.
     */
    private Long id;

    /**
     * The username of the user.
     */
    private String username;

    /**
     * The role of the user.
     */
    private Role role;

}
