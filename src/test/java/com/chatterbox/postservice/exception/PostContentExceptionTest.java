package com.chatterbox.postservice.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class PostContentExceptionTest {

    @Test
    void instance() {
        String errorMessage = "message";
        PostContentException exception = new PostContentException(errorMessage);
        assertNotNull(exception);
        assertEquals(errorMessage, exception.getMessage());
    }
}
