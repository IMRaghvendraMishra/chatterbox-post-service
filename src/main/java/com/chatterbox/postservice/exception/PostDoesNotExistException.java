package com.chatterbox.postservice.exception;

/**
 * Exception thrown when a requested post cannot be found in the ChatterBox platform.
 *
 * <p>This custom unchecked exception is used in scenarios where a post lookup
 * by ID or user reference fails due to non-existence.</p>
 *
 * <p>
 * Handled by {@link com.chatterbox.postservice.exception.handler.GlobalExceptionHandler}
 * to return a 404 Not Found HTTP response.
 * </p>
 */
public class PostDoesNotExistException extends RuntimeException {
    public PostDoesNotExistException(String message) {
        super(message);
    }
}
