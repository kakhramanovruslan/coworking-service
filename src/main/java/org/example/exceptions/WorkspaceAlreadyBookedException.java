package org.example.exceptions;

/**
 * Exception thrown when attempting to book a workspace that is already booked.
 */
public class WorkspaceAlreadyBookedException extends RuntimeException {

    /**
     * Constructs a new WorkspaceAlreadyBookedException with the specified detail message.
     *
     * @param message the detail message (which is saved for later retrieval by the getMessage() method).
     */
    public WorkspaceAlreadyBookedException(String message) {
        super(message);
    }
}
