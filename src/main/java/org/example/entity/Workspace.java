package org.example.entity;

import lombok.Data;

import java.util.List;

@Data
public class Workspace {
    static Long generatedId = 0L;
    private Long id;
    private String name;

    public Workspace(String name) {
        generatedId++;
        this.id = generatedId;
        this.name = name;
    }
}
