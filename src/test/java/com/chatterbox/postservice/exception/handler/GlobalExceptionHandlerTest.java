package com.chatterbox.postservice.exception.handler;

import com.chatterbox.postservice.exception.InvalidUserException;
import com.chatterbox.postservice.exception.MandatoryFieldException;
import com.chatterbox.postservice.exception.PostContentException;
import com.chatterbox.postservice.exception.PostDoesNotExistException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GlobalExceptionHandlerTest.DummyController.class)
@Import(GlobalExceptionHandler.class)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @RestController
    static class DummyController {

        @GetMapping("/invalid-user")
        public void throwInvalidUserException() {
            throw new InvalidUserException("User not found");
        }

        @GetMapping("/post-content-error")
        public void throwPostContentException() {
            throw new PostContentException("Invalid content");
        }

        @GetMapping("/post-not-found")
        public void throwPostDoesNotExistException() {
            throw new PostDoesNotExistException("Post not found");
        }

        @GetMapping("/missing-field")
        public void throwMandatoryFieldException() {
            throw new MandatoryFieldException("Required field is missing");
        }

        @GetMapping("/generic-error")
        public void throwGenericException() {
            throw new RuntimeException("Something went wrong");
        }
    }

   /* @Test
    void handleInvalidUserException_shouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/invalid-user"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User not found"))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"));
    }*/

   /* @Test
    void handlePostContentException_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/post-content-error"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid content"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"));
    }*/

    /*@Test
    void handlePostDoesNotExistException_shouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/post-not-found"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Post not found"))
                .andExpect(jsonPath("$.status").value(404));
    }*/

    /*@Test
    void handleMandatoryFieldException_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/missing-field"))
                .andExpect(status().isBadRequest());
    }*/

    @Test
    void handleGenericException_shouldReturnInternalServerError() throws Exception {
        mockMvc.perform(get("/generic-error"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("An unexpected error occurred."))
                .andExpect(jsonPath("$.status").value(500));
    }
}
