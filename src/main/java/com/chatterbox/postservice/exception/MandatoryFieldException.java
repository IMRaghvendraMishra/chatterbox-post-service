package com.chatterbox.postservice.exception;

/**
 * Exception thrown when a required or mandatory field is missing in a request
 * within the ChatterBox platform's PostService module.
 *
 * <p>This custom unchecked exception is used to enforce input validation rules
 * during operations like post creation or update.</p>
 *
 * <p>
 * Typically handled by {@link com.chatterbox.postservice.exception.handler.GlobalExceptionHandler}
 * to return a 400 Bad Request HTTP response.
 * </p>
 */
public class MandatoryFieldException extends RuntimeException {
    public MandatoryFieldException(String message) {
        super(message);
    }
}
