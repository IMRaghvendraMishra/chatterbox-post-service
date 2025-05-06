package com.chatterbox.postservice.exception;

/**
 * Exception thrown when an operation references a user that is invalid or does not exist
 * in the ChatterBox platform.
 *
 * <p>This is a custom unchecked exception used to signal user validation failures,
 * particularly during post creation or retrieval operations.</p>
 *
 * <p>
 * Typically handled by {@link com.chatterbox.postservice.exception.handler.GlobalExceptionHandler}
 * to return a 404 Not Found HTTP response.
 * </p>
 */
public class InvalidUserException extends RuntimeException {
    public InvalidUserException(String message) {
        super(message);
    }
}
