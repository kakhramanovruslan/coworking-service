package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.entity.types.Role;

/**
 * Represents authentication information containing a username and a role.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Authentication {

    /**
     * The username used for authentication.
     */
    private String username;

    /**
     * The role associated with the authenticated user.
     */
    private Role role;
}
