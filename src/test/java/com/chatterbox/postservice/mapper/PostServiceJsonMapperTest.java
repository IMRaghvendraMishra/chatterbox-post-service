package com.chatterbox.postservice.mapper;

import com.chatterbox.postservice.model.Post;
import com.chatterbox.postservice.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PostServiceJsonMapperTest {

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private PostServiceJsonMapper mapper;

    private Post post;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        post = new Post();
        post.setPostId("p001");
        post.setUsername("jane_doe");
        post.setContent("This is a test post");

        user = new User();
        user.setUserName("jane_doe");
        user.setEmail("jane@example.com");
    }

    @Test
    void postEventToJson_success() throws Exception {
        String expectedJson = "{\"postId\":\"p001\",\"username\":\"jane_doe\",\"content\":\"This is a test post\"}";
        when(objectMapper.writeValueAsString(post)).thenReturn(expectedJson);

        String jsonResult = mapper.postEventToJson(post);

        assertEquals(expectedJson, jsonResult);
        verify(objectMapper, times(1)).writeValueAsString(post);
    }

    @Test
    void postEventToJson_errorHandling() throws Exception {
        when(objectMapper.writeValueAsString(post)).thenThrow(new JsonProcessingException("error") {});

        String jsonResult = mapper.postEventToJson(post);

        assertEquals("", jsonResult);
        verify(objectMapper, times(1)).writeValueAsString(post);
    }

    @Test
    void jsonToUser_success() throws Exception {
        String json = "{\"userName\":\"jane_doe\",\"email\":\"jane@example.com\"}";
        when(objectMapper.readValue(json, User.class)).thenReturn(user);

        User result = mapper.jsonToUser(json);

        assertNotNull(result);
        assertEquals("jane_doe", result.getUserName());
        assertEquals("jane@example.com", result.getEmail());
        verify(objectMapper, times(1)).readValue(json, User.class);
    }

    @Test
    void jsonToUser_errorHandling() throws Exception {
        String invalidJson = "{invalid json}";
        when(objectMapper.readValue(invalidJson, User.class)).thenThrow(new JsonProcessingException("error") {});

        User result = mapper.jsonToUser(invalidJson);

        assertNull(result);
        verify(objectMapper, times(1)).readValue(invalidJson, User.class);
    }
}
