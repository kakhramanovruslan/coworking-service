package org.example.exceptions;

/**
 * Exception class representing an authentication-related error.
 * Thrown when there is an issue during user authentication.
 */
public class AuthenticationException extends RuntimeException {

    /**
     * Constructs a new AuthenticationException with the specified detail message.
     *
     * @param message the detail message (which is saved for later retrieval by the getMessage() method).
     */
    public AuthenticationException(String message) {
        super(message);
    }
}