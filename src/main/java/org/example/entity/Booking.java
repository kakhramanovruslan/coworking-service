package org.example.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Booking {

    private Long id;

    private Long workspaceId;

    private Long userId;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

}
