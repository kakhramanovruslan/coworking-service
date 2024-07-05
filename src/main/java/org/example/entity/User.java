package org.example.entity;

import lombok.*;
import org.example.entity.types.Role;


/**
 * Represents a user with information such as a unique identifier, username, password.
 *
 * @author ruslan
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    /**
     * The unique identifier for the user.
     */
    private Long id;

    /**
     * The username of the user.
     */
    private String username;

    /**
     * The password of the user.
     */
    private String password;

    /**
     * The role assigned to the user. Default value is USER.
     */
    @Builder.Default
    Role role = Role.USER;

}
