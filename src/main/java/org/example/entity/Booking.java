package org.example.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Booking {

    static Long generatedId= Long.valueOf(0);

    Long id;
    String workspaceName;

    String username;
    LocalDateTime startTime;
    LocalDateTime endTime;

    public Booking(String workspaceName, LocalDateTime startTime, LocalDateTime endTime, String username) {
        generatedId++;
        this.id = generatedId;
        System.out.println(id);
        this.workspaceName = workspaceName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.username = username;
    }
}
