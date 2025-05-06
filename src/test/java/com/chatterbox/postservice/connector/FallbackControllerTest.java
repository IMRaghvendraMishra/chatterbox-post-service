package com.chatterbox.postservice.connector;

import com.chatterbox.postservice.controller.FallbackController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FallbackController.class)
public class FallbackControllerTest {

    @Autowired
    private MockMvc mockMvc;  // Mock MVC to send requests and verify responses

    @Test
    public void handleInvalidPath() throws Exception {
        // Act
        ResultActions result = mockMvc.perform(get("/wrong/url"));

        // Assert
        result.andExpect(status().isNotFound());
    }
}
