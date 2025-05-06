package com.chatterbox.postservice.validator;

import com.chatterbox.postservice.connector.HttpClientConnector;
import com.chatterbox.postservice.exception.InvalidUserException;
import com.chatterbox.postservice.exception.MandatoryFieldException;
import com.chatterbox.postservice.exception.PostContentException;
import com.chatterbox.postservice.model.Post;
import com.chatterbox.postservice.model.User;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
@AllArgsConstructor
public class PostServiceValidator {

    @Value("${post.content.min}")
    private int minPostContentLength;
    @Value("${post.content.max}")
    private int maxPostContentLength;

    @Autowired private HttpClientConnector httpClientConnector;

    /**
     * Validate whether user exist
     * Validate post content
     */
    public void validatePost(Post post) {
        User user = httpClientConnector.getUserByUsername(post.getUsername());
        if(user != null && user.getUserName().equals(post.getUsername())) {
            validatePostContent(post.getContent());
        }
    }

    public void validateUsername(String username) {
        if(Strings.isBlank(username)) {
            throw new MandatoryFieldException("username cannot be empty or null");
        }
        User user = httpClientConnector.getUserByUsername(username);
        if(user == null || !user.getUserName().equalsIgnoreCase(username)) {
            throw new InvalidUserException("User with username " + username + " does not exist");
        }
    }

    public void validatePostId(String postId) {
        if(Strings.isBlank(postId)) {
            throw new MandatoryFieldException("postId cannot be empty or null");
        }

    }

    private void validatePostContent(String content) {
        if(Strings.isBlank(content)) {
            throw new PostContentException("Post content cannot be null or empty");
        } else if(content.length() < minPostContentLength || content.length() > maxPostContentLength) {
            throw new PostContentException("Post content should be minimum " + minPostContentLength
                    + " characters and maximum " + maxPostContentLength + " characters");
        }
    }



}
