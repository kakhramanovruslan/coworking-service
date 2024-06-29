package org.example.entity;

<<<<<<< HEAD
import lombok.*;

/**
 * Represents a user with information such as a unique identifier, username, password.
 *
 * @author ruslan
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    /**
     * The unique identifier for the user.
     */
    private Long id;

    /**
     * The username of the user.
     */
    private String username;

    /**
     * The password of the user.
     */
    private String password;

=======
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

>>>>>>> 43e3611c8f9b95f07c7653312542905fd21780d8
}
