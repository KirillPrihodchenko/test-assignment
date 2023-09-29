package com.testAssignment.demo.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

@RestControllerAdvice
public class DefaultExceptionHandler {

    @ExceptionHandler({IllegalArgumentException.class, ResponseStatusException.class})
    public ResponseEntity<ApiError> handleInvalidData(
            ResponseStatusException ex, HttpServletRequest hsr) {

        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                hsr.getRequestURI(),
                ex.getMessage(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({EntityNotFoundException.class, RuntimeException.class})
    public ResponseEntity<ApiError> InvalidEntityData(
            ResponseStatusException ex, HttpServletRequest hsr) {

        ApiError apiError = new ApiError(
                HttpStatus.NOT_FOUND.value(),
                hsr.getRequestURI(),
                ex.getMessage(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({DateTimeParseException.class, DateTimeException.class})
    public ResponseEntity<ApiError> handeInvalidTime(
            ResponseStatusException ex, HttpServletRequest hsr) {

        ApiError apiError = new ApiError(
                HttpStatus.NO_CONTENT.value(),
                hsr.getRequestURI(),
                ex.getMessage(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(apiError, HttpStatus.NO_CONTENT);
    }
}