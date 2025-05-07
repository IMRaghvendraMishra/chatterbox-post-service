package com.chatterbox.postservice.controller;

import com.chatterbox.postservice.messaging.PostEventProducer;
import com.chatterbox.postservice.model.Post;
import com.chatterbox.postservice.service.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostController.class)
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostService postService;

    @MockBean
    private PostEventProducer postEventProducer;

    @Autowired
    private ObjectMapper objectMapper;

    private Post samplePost;

    @BeforeEach
    void setUp() {
        samplePost = new Post();
        samplePost.setPostId("1");
        samplePost.setUsername("john_doe");
        samplePost.setContent("This is a test post.");
    }

    @Test
    void createPost() throws Exception {
        // Arrange
        String postJson = objectMapper.writeValueAsString(samplePost);
        String successMessage = "Post created successfully for username john_doe";
        when(postService.createPost(any(Post.class))).thenReturn(successMessage);

        // Act
        ResultActions result = mockMvc.perform(post("/api/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(postJson));

        // Assert
        result.andExpect(status().isCreated())
                .andExpect(content().string(successMessage));
        verify(postService, times(1)).createPost(any(Post.class));
        verify(postEventProducer, times(1)).sendPostCreatedEvent(any(Post.class));
    }

    @Test
    void updatePost() throws Exception {
        // Arrange
        String postJson = objectMapper.writeValueAsString(samplePost);
        String successMessage = "Post updated successfully";
        when(postService.updatePost(any(Post.class))).thenReturn(successMessage);

        // Act
        ResultActions result = mockMvc.perform(put("/api/posts/{postId}", samplePost.getPostId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(postJson));

        // Assert
        result.andExpect(status().isOk())
                .andExpect(content().string(successMessage));
        verify(postService, times(1)).updatePost(any(Post.class));
    }

    @Test
    void getPostById() throws Exception {
        // Arrange
        when(postService.getPostByPostId(samplePost.getPostId())).thenReturn(samplePost);

        // Act
        ResultActions result = mockMvc.perform(get("/api/posts/{postId}", samplePost.getPostId()));

        // Assert
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.postId").value(samplePost.getPostId()))
                .andExpect(jsonPath("$.username").value(samplePost.getUsername()))
                .andExpect(jsonPath("$.content").value(samplePost.getContent()));
        verify(postService, times(1)).getPostByPostId(samplePost.getPostId());
    }

    @Test
    void getPostsByUsername() throws Exception {
        // Arrange
        when(postService.getPostsByUsername("john_doe")).thenReturn(List.of(samplePost));

        // Act
        ResultActions result = mockMvc.perform(get("/api/posts/user/{username}", "john_doe"));

        // Assert
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].postId").value(samplePost.getPostId()))
                .andExpect(jsonPath("$[0].username").value(samplePost.getUsername()));
        verify(postService, times(1)).getPostsByUsername("john_doe");
    }

    @Test
    void getAllPosts() throws Exception {
        // Arrange
        when(postService.getAllPosts()).thenReturn(List.of(samplePost));

        // Act
        ResultActions result = mockMvc.perform(get("/api/posts"));

        // Assert
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].postId").value(samplePost.getPostId()))
                .andExpect(jsonPath("$[0].username").value(samplePost.getUsername()));
        verify(postService, times(1)).getAllPosts();
    }

    @Test
    void deletePostByPostId() throws Exception {
        // Arrange
        String successMessage = "Post with id " + samplePost.getPostId() + " deleted";
        when(postService.deletePostByPostId(samplePost.getPostId())).thenReturn(successMessage);

        // Act
        ResultActions result = mockMvc.perform(delete("/api/posts/{postId}", samplePost.getPostId()));

        // Assert
        result.andExpect(status().isOk())
                .andExpect(content().string(successMessage));
        verify(postService, times(1)).deletePostByPostId(samplePost.getPostId());
    }

    @Test
    void deletePostsByUsername() throws Exception {
        // Arrange
        String successMessage = "All posts by john_doe deleted";
        when(postService.deletePostByUsername("john_doe")).thenReturn(successMessage);

        // Act
        ResultActions result = mockMvc.perform(delete("/api/posts/user/{username}", "john_doe"));

        // Assert
        result.andExpect(status().isOk())
                .andExpect(content().string(successMessage));
        verify(postService, times(1)).deletePostByUsername("john_doe");
    }

    @Test
    void deleteAllPosts() throws Exception {
        // Arrange
        String successMessage = "All posts deleted from the system";
        when(postService.deleteAllPosts()).thenReturn(successMessage);

        // Act
        ResultActions result = mockMvc.perform(delete("/api/posts"));

        // Assert
        result.andExpect(status().isOk())
                .andExpect(content().string(successMessage));
        verify(postService, times(1)).deleteAllPosts();
    }
}

