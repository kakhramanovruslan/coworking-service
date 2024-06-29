package org.example.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Represents a workspace with information such as a unique identifier, name.
 *
 * @author ruslan
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Workspace {

    /**
     * The unique identifier for the workspace.
     */
    private Long id;

    /**
     * The name of workspace.
     */
    private String name;

}
