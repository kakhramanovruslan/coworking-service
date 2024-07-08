package org.example.exceptions;

/**
 * Exception thrown when a requested workspace is not found.
 */
public class WorkspaceNotFoundException extends RuntimeException {

    /**
     * Constructs a new WorkspaceNotFoundException with the specified detail message.
     *
     * @param message the detail message (which is saved for later retrieval by the getMessage() method).
     */
    public WorkspaceNotFoundException(String message) {
        super(message);
    }
}
