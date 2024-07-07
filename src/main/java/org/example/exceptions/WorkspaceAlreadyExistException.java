package org.example.exceptions;

public class WorkspaceAlreadyExistException extends RuntimeException {
    public WorkspaceAlreadyExistException(String message) {
        super(message);
    }
}
