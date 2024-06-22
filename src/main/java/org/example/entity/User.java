package org.example.entity;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
public class User {
    static Long generatedId = 0L;

    private Long id;


    private String username;
    private String password;

    public User(String username, String password) {
        generatedId++;
        this.id = generatedId;
        this.username = username;
        this.password = password;
    }

}
