package com.chatterbox.postservice.exception.handler;

import com.chatterbox.postservice.exception.InvalidUserException;
import com.chatterbox.postservice.exception.MandatoryFieldException;
import com.chatterbox.postservice.exception.PostContentException;
import com.chatterbox.postservice.exception.PostDoesNotExistException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for the PostService module of the ChatterBox platform.
 * <p>
 * Catches and processes application-specific and generic exceptions thrown by
 * REST controllers. Converts exceptions into standardized HTTP responses containing
 * useful metadata such as timestamp, HTTP status, and error messages.
 * </p>
 *
 * <p><strong>Handled Exceptions:</strong></p>
 * <ul>
 *     <li>{@link com.chatterbox.postservice.exception.InvalidUserException} - Returned when a referenced user is invalid or not found.</li>
 *     <li>{@link com.chatterbox.postservice.exception.PostContentException} - Thrown for invalid or malformed post content.</li>
 *     <li>{@link com.chatterbox.postservice.exception.PostDoesNotExistException} - Thrown when a requested post ID does not exist.</li>
 *     <li>{@link com.chatterbox.postservice.exception.MandatoryFieldException} - Raised when required fields are missing in the request.</li>
 *     <li>{@link java.lang.Exception} - Catch-all handler for all other unhandled exceptions.</li>
 * </ul>
 *
 * <p>
 * Each exception is mapped to an appropriate {@link org.springframework.http.HttpStatus} code and
 * returned as a structured JSON response.
 * </p>
 */

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidUserException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidUserException(InvalidUserException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PostContentException.class)
    public ResponseEntity<Map<String, Object>> handlePostContentException(PostContentException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PostDoesNotExistException.class)
    public ResponseEntity<Map<String, Object>> handlePostDoesNotExistException(PostDoesNotExistException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    // Handle MandatoryFieldException
    @ExceptionHandler(MandatoryFieldException.class)
    public ResponseEntity<Map<String, Object>> handleMandatoryField(MandatoryFieldException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // Catch-all for unhandled exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        return buildErrorResponse("An unexpected error occurred.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<Map<String, Object>> buildErrorResponse(String message, HttpStatus status) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        return new ResponseEntity<>(body, status);
    }
}
