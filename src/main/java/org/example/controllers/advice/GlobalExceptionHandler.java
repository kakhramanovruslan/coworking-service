package org.example.controllers.advice;

import org.example.dto.AppExceptionResponse;
import org.example.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;

/**
 * Global exception handler for handling exceptions across the whole application.
 * This class provides methods to handle specific exceptions and return appropriate
 * HTTP responses.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles AuthenticationException and returns an HTTP 401 (Unauthorized) response.
     *
     * @param exception the exception to handle
     * @return ResponseEntity with HTTP status 401 and the exception message
     */
    @ExceptionHandler(AuthenticationException.class)
    ResponseEntity<AppExceptionResponse> handleAuthenticationException(AuthenticationException exception) {
        return buildExceptionResponse(HttpStatus.UNAUTHORIZED, exception.getMessage());
    }

    /**
     * Handles NotValidArgumentException and returns an HTTP 400 (Bad Request) response.
     *
     * @param exception the exception to handle
     * @return ResponseEntity with HTTP status 400 and the exception message
     */
    @ExceptionHandler(NotValidArgumentException.class)
    ResponseEntity<AppExceptionResponse> handleNotValidArgumentException(NotValidArgumentException exception) {
        return buildExceptionResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    /**
     * Handles RegisterException and returns an HTTP 400 (Bad Request) response.
     *
     * @param exception the exception to handle
     * @return ResponseEntity with HTTP status 400 and the exception message
     */
    @ExceptionHandler(RegisterException.class)
    ResponseEntity<AppExceptionResponse> handleRegisterException(RegisterException exception) {
        return buildExceptionResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    /**
     * Handles UserNotFoundException and returns an HTTP 400 (Bad Request) response.
     *
     * @param exception the exception to handle
     * @return ResponseEntity with HTTP status 400 and the exception message
     */
    @ExceptionHandler(UserNotFoundException.class)
    ResponseEntity<AppExceptionResponse> handleUserNotFoundException(UserNotFoundException exception) {
        return buildExceptionResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    /**
     * Handles InvalidCredentialsException and returns an HTTP 400 (Bad Request) response.
     *
     * @param exception the exception to handle
     * @return ResponseEntity with HTTP status 400 and the exception message
     */
    @ExceptionHandler(InvalidCredentialsException.class)
    ResponseEntity<AppExceptionResponse> handleInvalidCredentialsException(InvalidCredentialsException exception) {
        return buildExceptionResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    /**
     * Handles WorkspaceAlreadyBookedException and returns an HTTP 400 (Bad Request) response.
     *
     * @param exception the exception to handle
     * @return ResponseEntity with HTTP status 400 and the exception message
     */
    @ExceptionHandler(WorkspaceAlreadyBookedException.class)
    ResponseEntity<AppExceptionResponse> handleWorkspaceAlreadyBookedException(WorkspaceAlreadyBookedException exception) {
        return buildExceptionResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    /**
     * Handles WorkspaceAlreadyExistException and returns an HTTP 400 (Bad Request) response.
     *
     * @param exception the exception to handle
     * @return ResponseEntity with HTTP status 400 and the exception message
     */
    @ExceptionHandler(WorkspaceAlreadyExistException.class)
    ResponseEntity<AppExceptionResponse> handleWorkspaceAlreadyExistException(WorkspaceAlreadyExistException exception) {
        return buildExceptionResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    /**
     * Handles WorkspaceNotFoundException and returns an HTTP 400 (Bad Request) response.
     *
     * @param exception the exception to handle
     * @return ResponseEntity with HTTP status 400 and the exception message
     */
    @ExceptionHandler(WorkspaceNotFoundException.class)
    ResponseEntity<AppExceptionResponse> handleWorkspaceNotFoundException(WorkspaceNotFoundException exception) {
        return buildExceptionResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    /**
     * Handles AccessDeniedException and returns an HTTP 403 (Forbidden) response.
     *
     * @param exception the exception to handle
     * @return ResponseEntity with HTTP status 403 and the exception message
     */
    @ExceptionHandler(AccessDeniedException.class)
    ResponseEntity<AppExceptionResponse> handleAccessDeniedException(AccessDeniedException exception) {
        return buildExceptionResponse(HttpStatus.FORBIDDEN, exception.getMessage());
    }

    /**
     * Builds a ResponseEntity with the specified HTTP status and error message.
     * @param status The HTTP status.
     * @param message The error message.
     * @return ResponseEntity with the specified status and AppExceptionResponse body.
     */
    private ResponseEntity<AppExceptionResponse> buildExceptionResponse(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(AppExceptionResponse.builder()
                .status(status.value())
                .message(message)
                .build());
    }
}