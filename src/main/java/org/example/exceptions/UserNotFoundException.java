package org.example.exceptions;

public class UserNotFoundException extends RuntimeException {

    /**
     * Constructs a new WorkspaceNotFoundException with the specified detail message.
     *
     * @param message the detail message (which is saved for later retrieval by the getMessage() method).
     */
    public UserNotFoundException(String message) {
        super(message);
    }
}
