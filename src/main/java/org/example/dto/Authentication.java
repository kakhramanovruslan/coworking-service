package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.entity.types.Role;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Authentication {
    private String login;
    private Role role;
}
