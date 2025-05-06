package com.chatterbox.postservice.validator;

import com.chatterbox.postservice.connector.HttpClientConnector;
import com.chatterbox.postservice.exception.InvalidUserException;
import com.chatterbox.postservice.exception.MandatoryFieldException;
import com.chatterbox.postservice.exception.PostContentException;
import com.chatterbox.postservice.model.Post;
import com.chatterbox.postservice.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostServiceValidatorTest {

    @InjectMocks
    private PostServiceValidator postServiceValidator;

    @Mock
    private HttpClientConnector httpClientConnector;

    @Mock
    private User mockUser;

    private Post mockPost;

    @BeforeEach
    void setUp() {
        postServiceValidator.minPostContentLength = 5;
        postServiceValidator.maxPostContentLength = 30;
        mockPost = new Post();
        mockPost.setPostId("12345");
        mockPost.setUsername("john_doe");
        mockPost.setContent("This is a test post.");
    }

    @Test
    void validatePost_ValidPost() {
        // Arrange: Mock HttpClientConnector to return a valid user
        when(httpClientConnector.getUserByUsername(eq(mockPost.getUsername()))).thenReturn(mockUser);
        when(mockUser.getUserName()).thenReturn(mockPost.getUsername());

        // Act & Assert: No exception should be thrown
        assertDoesNotThrow(() -> postServiceValidator.validatePost(mockPost));
    }

    @Test
    void validatePostContent_EmptyContent() {
        // Arrange: Set empty content in mockPost
        when(httpClientConnector.getUserByUsername(eq(mockPost.getUsername()))).thenReturn(mockUser);
        when(mockUser.getUserName()).thenReturn(mockPost.getUsername());
        mockPost.setContent("");

        // Act & Assert: Expect PostContentException to be thrown
        PostContentException exception = assertThrows(PostContentException.class, () -> {
            postServiceValidator.validatePost(mockPost);
        });

        assertEquals("Post content cannot be null or empty", exception.getMessage());
    }

    @Test
    void validatePostContent_ShortContent() {
        // Arrange: Set content shorter than the min length
        when(httpClientConnector.getUserByUsername(mockPost.getUsername())).thenReturn(mockUser);
        when(mockUser.getUserName()).thenReturn(mockPost.getUsername());
        mockPost.setContent("abc");

        // Act & Assert: Expect PostContentException to be thrown
        PostContentException exception = assertThrows(PostContentException.class, () -> {
            postServiceValidator.validatePost(mockPost);
        });

        assertEquals("Post content should be minimum 5 characters and maximum 30 characters", exception.getMessage());
    }

    @Test
    void validatePostContent_LongContent() {
        // Arrange: Set content longer than the max length
        when(httpClientConnector.getUserByUsername(eq(mockPost.getUsername()))).thenReturn(mockUser);
        when(mockUser.getUserName()).thenReturn(mockPost.getUsername());
        mockPost.setContent("A very long post content that exceeds the maximum length allowed for posts...");

        // Act & Assert: Expect PostContentException to be thrown
        PostContentException exception = assertThrows(PostContentException.class, () -> {
            postServiceValidator.validatePost(mockPost);
        });

        assertEquals("Post content should be minimum 5 characters and maximum 30 characters", exception.getMessage());
    }

    @Test
    void validateUsername_ValidUsername() {
        // Arrange: Mock HttpClientConnector to return a valid user
        when(httpClientConnector.getUserByUsername(eq(mockPost.getUsername()))).thenReturn(mockUser);
        when(mockUser.getUserName()).thenReturn(mockPost.getUsername());

        // Act & Assert: No exception should be thrown
        assertDoesNotThrow(() -> postServiceValidator.validateUsername(mockPost.getUsername()));
    }

    @Test
    void validateUsername_EmptyUsername() {
        // Act & Assert: Expect MandatoryFieldException to be thrown for empty username
        MandatoryFieldException exception = assertThrows(MandatoryFieldException.class, () -> {
            postServiceValidator.validateUsername("");
        });

        assertEquals("username cannot be empty or null", exception.getMessage());
    }

    @Test
    void validateUsername_InvalidUsername() {
        // Arrange: Mock HttpClientConnector to return null user (non-existing user)
        when(httpClientConnector.getUserByUsername(eq(mockPost.getUsername()))).thenReturn(null);

        // Act & Assert: Expect InvalidUserException to be thrown for invalid username
        InvalidUserException exception = assertThrows(InvalidUserException.class, () -> {
            postServiceValidator.validateUsername(mockPost.getUsername());
        });

        assertEquals("User with username john_doe does not exist", exception.getMessage());
    }

    @Test
    void validatePostId_EmptyPostId() {
        // Act & Assert: Expect MandatoryFieldException to be thrown for empty postId
        MandatoryFieldException exception = assertThrows(MandatoryFieldException.class, () -> {
            postServiceValidator.validatePostId("");
        });

        assertEquals("postId cannot be empty or null", exception.getMessage());
    }

    @Test
    void validatePostId_ValidPostId() {
        // Act & Assert: No exception should be thrown for valid postId
        assertDoesNotThrow(() -> postServiceValidator.validatePostId("12345"));
    }
}
