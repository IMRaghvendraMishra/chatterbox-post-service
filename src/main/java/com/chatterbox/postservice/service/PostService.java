package com.chatterbox.postservice.service;

import com.chatterbox.postservice.exception.PostDoesNotExistException;
import com.chatterbox.postservice.model.Post;
import com.chatterbox.postservice.repository.PostRepository;
import com.chatterbox.postservice.validator.PostServiceValidator;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

/**
 * Service layer responsible for managing operations related to posts within the ChatterBox platform.
 * <p>
 * This class handles the core business logic for creating, updating, retrieving, and deleting posts.
 * It ensures that all input data is validated through the {@link PostServiceValidator} before being persisted
 * or manipulated via the {@link PostRepository}.
 * </p>
 *
 * <p>
 * Responsibilities include:
 * <ul>
 *   <li>Validating and saving new posts</li>
 *   <li>Updating existing posts</li>
 *   <li>Fetching posts by post ID or username</li>
 *   <li>Deleting individual posts or bulk deletions by username or system-wide</li>
 * </ul>
 * </p>
 *
 * <p>
 * The service also leverages logging for key actions and errors, ensuring better monitoring and debugging.
 * </p>
 */
@Service
@AllArgsConstructor
@Log4j2
public class PostService {

    private PostRepository postRepository;
    private PostServiceValidator validator;

    public String createPost(Post post) {
        validator.validatePost(post);
        post.setTimestamp(Instant.now());
        postRepository.save(post);
        return String.format("A new post with post id %s created by user %s", post.getPostId(), post.getUsername());
    }

    public String updatePost(Post post) {
        // validate if post already exist
        getPostByPostId(post.getPostId());
        validator.validatePost(post);
        post.setTimestamp(Instant.now());
        postRepository.save(post);
        return String.format("A post with post id %s is updated by username %s", post.getPostId(), post.getUsername());
    }

    public List<Post> getPostsByUsername(String username) {
        validator.validateUsername(username);
    	return postRepository.findByUsername(username);
    }

    public Post getPostByPostId(String postId) {
        validator.validatePostId(postId);
        return postRepository.findById(postId).orElseThrow(() ->
                new PostDoesNotExistException("Post with ID " + postId + " not found"));
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public String deletePostByPostId(String postId) {
        validator.validatePostId(postId);
        postRepository.findById(postId).ifPresent(postRepository::delete);
        log.info("Deleted post with ID: {}", postId);
        return "Deleted post with ID: " + postId;
    }

    public String deletePostByUsername(String username) {
        validator.validateUsername(username);

        var userPosts = postRepository.findByUsername(username);
        if (userPosts != null && !userPosts.isEmpty()) {
            userPosts.forEach(postRepository::delete);
        }
        log.info("All user post deleted");
        return "All user post deleted";
    }

    public String deleteAllPosts() {
        postRepository.deleteAll();
        return "All posts deleted";
    }
}
