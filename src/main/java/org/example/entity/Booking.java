package org.example.entity;

<<<<<<< HEAD
<<<<<<< HEAD
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Represents a booking with information such as a unique identifier, workspaceId, userId, startTime, endTime.
 *
 * @author ruslan
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Booking {

    /**
     * The unique identifier for the booking.
     */
    private Long id;

    /**
     * The unique identifier for the workspace associated with the booking.
     */
    private Long workspaceId;

    /**
     * The unique identifier for the user associated with the booking.
     */
    private Long userId;

    /**
     * The start time of booking.
     */
    private LocalDateTime startTime;

    /**
     * The end time of booking.
     */
    private LocalDateTime endTime;

=======
=======
import lombok.AllArgsConstructor;
import lombok.Builder;
>>>>>>> ylab_lab2
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Represents a booking with information such as a unique identifier, workspaceId, userId, startTime, endTime.
 *
 * @author ruslan
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Booking {

    /**
     * The unique identifier for the booking.
     */
    private Long id;

    /**
     * The unique identifier for the workspace associated with the booking.
     */
    private Long workspaceId;

    /**
     * The unique identifier for the user associated with the booking.
     */
    private Long userId;

    /**
     * The start time of booking.
     */
    private LocalDateTime startTime;

    /**
     * The end time of booking.
     */
    private LocalDateTime endTime;

<<<<<<< HEAD
    public Booking(String workspaceName, LocalDateTime startTime, LocalDateTime endTime, String username) {
        generatedId++;
        this.id = generatedId;
        System.out.println(id);
        this.workspaceName = workspaceName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.username = username;
    }
>>>>>>> 43e3611c8f9b95f07c7653312542905fd21780d8
=======
>>>>>>> ylab_lab2
}
