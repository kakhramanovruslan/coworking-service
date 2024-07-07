package org.example.dto;

import lombok.*;
import org.example.entity.types.Role;

@Getter
@Setter
@Builder
public class UserDTO {

    private Long id;
    private String username;
    private Role role;

}
