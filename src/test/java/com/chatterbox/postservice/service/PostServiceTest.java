package com.chatterbox.postservice.service;

import com.chatterbox.postservice.exception.PostDoesNotExistException;
import com.chatterbox.postservice.model.Post;
import com.chatterbox.postservice.repository.PostRepository;
import com.chatterbox.postservice.validator.PostServiceValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

    @InjectMocks
    private PostService postService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private PostServiceValidator postServiceValidator;

    private Post mockPost;

    @BeforeEach
    void setUp() {
        mockPost = new Post();
        mockPost.setPostId("12345");
        mockPost.setUsername("john_doe");
        mockPost.setContent("This is a test post.");
        mockPost.setTimestamp(Instant.now());
    }

    @Test
    void createPost() {
        // Arrange: Mock validator and repository
        doNothing().when(postServiceValidator).validatePost(any(Post.class));
        when(postRepository.save(any(Post.class))).thenReturn(mockPost);

        // Act
        String result = postService.createPost(mockPost);

        // Assert
        assertEquals("A new post with post id 12345 created by user john_doe", result);
        verify(postRepository, times(1)).save(mockPost);
    }

    @Test
    void updatePost() {
        // Arrange: Mock validator, repository, and existing post
        when(postRepository.findById(eq(mockPost.getPostId()))).thenReturn(Optional.of(mockPost));
        doNothing().when(postServiceValidator).validatePost(any(Post.class));
        when(postRepository.save(any(Post.class))).thenReturn(mockPost);

        // Act
        String result = postService.updatePost(mockPost);

        // Assert
        assertEquals("A post with post id 12345 is updated by username john_doe", result);
        verify(postRepository, times(1)).save(mockPost);
    }

    @Test
    void updatePost_PostDoesNotExist() {
        // Arrange: Mock validator and repository for non-existing post
        when(postRepository.findById(eq(mockPost.getPostId()))).thenReturn(Optional.empty());

        // Act and Assert: Expect PostDoesNotExistException
        PostDoesNotExistException exception = assertThrows(PostDoesNotExistException.class, () -> {
            postService.updatePost(mockPost);
        });

        assertEquals("Post with ID 12345 not found", exception.getMessage());
    }

    @Test
    void getPostsByUsername() {
        // Arrange: Mock repository behavior
        doNothing().when(postServiceValidator).validateUsername(eq(mockPost.getUsername()));
        when(postRepository.findByUsername(eq(mockPost.getUsername()))).thenReturn(List.of(mockPost));

        // Act
        List<Post> result = postService.getPostsByUsername(mockPost.getUsername());

        // Assert
        assertEquals(1, result.size());
        assertEquals(mockPost, result.get(0));
        verify(postRepository, times(1)).findByUsername(mockPost.getUsername());
    }

    @Test
    void getPostByPostId() {
        // Arrange: Mock repository behavior
        doNothing().when(postServiceValidator).validatePostId(eq(mockPost.getPostId()));
        when(postRepository.findById(eq(mockPost.getPostId()))).thenReturn(Optional.of(mockPost));

        // Act
        Post result = postService.getPostByPostId(mockPost.getPostId());

        // Assert
        assertEquals(mockPost, result);
        verify(postRepository, times(1)).findById(mockPost.getPostId());
    }

    @Test
    void getPostByPostId_PostDoesNotExist() {
        // Arrange: Mock repository behavior for non-existing post
        doNothing().when(postServiceValidator).validatePostId(eq(mockPost.getPostId()));
        when(postRepository.findById(eq(mockPost.getPostId()))).thenReturn(Optional.empty());

        // Act and Assert: Expect PostDoesNotExistException
        PostDoesNotExistException exception = assertThrows(PostDoesNotExistException.class, () -> {
            postService.getPostByPostId(mockPost.getPostId());
        });

        assertEquals("Post with ID 12345 not found", exception.getMessage());
    }

    @Test
    void deletePostByPostId() {
        // Arrange: Mock repository behavior
        doNothing().when(postServiceValidator).validatePostId(eq(mockPost.getPostId()));
        when(postRepository.findById(eq(mockPost.getPostId()))).thenReturn(Optional.of(mockPost));

        // Act
        String result = postService.deletePostByPostId(mockPost.getPostId());

        // Assert
        assertEquals("Deleted post with ID: 12345", result);
        verify(postRepository, times(1)).delete(mockPost);
    }

    @Test
    void deletePostByUsername() {
        // Arrange: Mock repository behavior
        doNothing().when(postServiceValidator).validateUsername(eq(mockPost.getUsername()));
        when(postRepository.findByUsername(eq(mockPost.getUsername()))).thenReturn(List.of(mockPost));

        // Act
        String result = postService.deletePostByUsername(mockPost.getUsername());

        // Assert
        assertEquals("All user post deleted", result);
        verify(postRepository, times(1)).delete(mockPost);
    }

    @Test
    void deleteAllPosts() {
        // Arrange: Mock repository behavior
        doNothing().when(postRepository).deleteAll();

        // Act
        String result = postService.deleteAllPosts();

        // Assert
        assertEquals("All posts deleted", result);
        verify(postRepository, times(1)).deleteAll();
    }
}
