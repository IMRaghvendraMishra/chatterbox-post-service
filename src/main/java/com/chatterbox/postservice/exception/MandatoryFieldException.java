package com.chatterbox.postservice.exception;

public class MandatoryFieldException extends RuntimeException {
    public MandatoryFieldException(String message) {
        super(message);
    }
}
