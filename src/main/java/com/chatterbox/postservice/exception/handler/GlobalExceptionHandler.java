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
