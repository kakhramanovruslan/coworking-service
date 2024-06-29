package org.example.entity;

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

}
