package org.example.exceptions;

public class WorkspaceAlreadyBookedException extends RuntimeException {

    public WorkspaceAlreadyBookedException(String message) {
        super(message);
    }
}
