package org.example.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Builder
@Getter
@Setter
public class WorkspaceRequest {
    
    /**
     * The name of workspace.
     */
    @NotNull(message = "Workspace cannot be null")
    @NotBlank(message = "Workspace cannot be blank")
    @Size(min = 3, max = 30, message = "Workspace length must be between 3 and 30 characters")
    private String name;

    @JsonCreator
    public WorkspaceRequest(@JsonProperty("name") String name) {
        this.name = name;
    };

}
