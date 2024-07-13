package org.example.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.*;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
public class BookingRequest {

    /**
     * The name of workspace for booking.
     */
    @NotNull(message = "Workspace name cannot be null")
    @NotBlank(message = "Workspace name cannot be blank")
    private String workspaceName;

    /**
     * The start time of booking.
     */
    @NotNull(message = "Start time cannot be null")
    @Future(message = "Start time must be in the future")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startTime;

    /**
     * The end time of booking.
     */
    @NotNull(message = "End time cannot be null")
    @Future(message = "End time must be in the future")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endTime;

    @JsonCreator
    public BookingRequest(@JsonProperty("workspaceName") String workspaceName,
                          @JsonProperty("startTime") LocalDateTime startTime,
                          @JsonProperty("endTime") LocalDateTime endTime) {
        this.workspaceName = workspaceName;
        this.startTime = startTime;
        this.endTime = endTime;
    }

}