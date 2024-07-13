package org.example.controllers;

import org.example.dto.AppExceptionResponse;
import org.example.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;


@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(AuthenticationException.class)
    ResponseEntity<AppExceptionResponse> handleAuthenticationException(AuthenticationException exception) {
        return buildExceptionResponse(HttpStatus.UNAUTHORIZED, exception.getMessage());
    }

    @ExceptionHandler(NotValidArgumentException.class)
    ResponseEntity<AppExceptionResponse> handleNotValidArgumentException(NotValidArgumentException exception) {
        return buildExceptionResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(RegisterException.class)
    ResponseEntity<AppExceptionResponse> handleRegisterException(RegisterException exception) {
        return buildExceptionResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    ResponseEntity<AppExceptionResponse> handleUserNotFoundException(UserNotFoundException exception) {
        return buildExceptionResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(WorkspaceAlreadyBookedException.class)
    ResponseEntity<AppExceptionResponse> handleWorkspaceAlreadyBookedException(WorkspaceAlreadyBookedException exception) {
        return buildExceptionResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(WorkspaceAlreadyExistException.class)
    ResponseEntity<AppExceptionResponse> handleWorkspaceAlreadyExistException(WorkspaceAlreadyExistException exception) {
        return buildExceptionResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(WorkspaceNotFoundException.class)
    ResponseEntity<AppExceptionResponse> handleWorkspaceAlreadyExistException(WorkspaceNotFoundException exception) {
        return buildExceptionResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

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