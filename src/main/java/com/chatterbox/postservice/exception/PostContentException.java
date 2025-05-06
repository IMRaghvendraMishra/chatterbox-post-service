package com.chatterbox.postservice.exception;

/**
 * Exception thrown when the content of a post is invalid, empty, or does not meet
 * the required format or constraints in the ChatterBox platform.
 *
 * <p>This custom unchecked exception is typically used during post creation or
 * update operations to ensure content validity.</p>
 *
 * <p>
 * Handled by {@link com.chatterbox.postservice.exception.handler.GlobalExceptionHandler}
 * to return a 400 Bad Request HTTP response.
 * </p>
 */
public class PostContentException extends RuntimeException {
    public PostContentException(String message) {
        super(message);
    }
}
