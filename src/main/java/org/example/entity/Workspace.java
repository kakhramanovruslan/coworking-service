package org.example.entity;

<<<<<<< HEAD
<<<<<<< HEAD
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

=======
=======
import lombok.AllArgsConstructor;
import lombok.Builder;
>>>>>>> ylab_lab2
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

<<<<<<< HEAD
    public Workspace(String name) {
        generatedId++;
        this.id = generatedId;
        this.name = name;
    }
>>>>>>> 43e3611c8f9b95f07c7653312542905fd21780d8
=======
>>>>>>> ylab_lab2
}
