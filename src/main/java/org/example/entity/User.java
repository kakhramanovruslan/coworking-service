package org.example.entity;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private Long id;

    private String username;

    private String password;

}
