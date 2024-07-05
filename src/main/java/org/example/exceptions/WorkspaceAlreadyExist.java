package org.example.exceptions;

public class WorkspaceAlreadyExist extends RuntimeException {
    public WorkspaceAlreadyExist(String message) {
        super(message);
    }
}
