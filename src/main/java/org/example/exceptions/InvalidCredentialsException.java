package org.example.exceptions;

/**
 * Exception class representing a credentials-related error.
 * Thrown when there is an issue during user authentication.
 */
public class InvalidCredentialsException extends RuntimeException {

    /**
     * Constructs a new InvalidCredentialsException with the specified detail message.
     *
     * @param message the detail message (which is saved for later retrieval by the getMessage() method).
     */
    public InvalidCredentialsException(String message) {
        super(message);
    }
}