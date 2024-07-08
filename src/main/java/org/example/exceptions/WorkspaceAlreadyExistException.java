package org.example.exceptions;

/**
 * Exception thrown when attempting to create a workspace that already exists.
 */
public class WorkspaceAlreadyExistException extends RuntimeException {
    /**
     * Constructs a new WorkspaceAlreadyExistException with the specified detail message.
     *
     * @param message the detail message (which is saved for later retrieval by the getMessage() method).
     */
    public WorkspaceAlreadyExistException(String message) {
        super(message);
    }
}
