package com.chatterbox.postservice.validator;

import com.chatterbox.postservice.connector.HttpClientConnector;
import com.chatterbox.postservice.exception.PostContentException;
import com.chatterbox.postservice.model.Post;
import com.chatterbox.postservice.model.User;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class PostServiceValidator {

    @Value("${post.content.min}")
    private int minPostContentLength;
    @Value("${post.content.max}")
    private int maxPostContentLength;

    @Autowired private HttpClientConnector httpClientConnector;

    public void validateNewPost(Post post) {
        User user = httpClientConnector.getUserByUsername(post.getUsername());
        if(user != null && user.getUserName().equals(post.getUsername())) {
            validatePostContent(post.getContent());
        }
    }

    public void validateUpdatePost(Post post) {

    }

    public void validateUsername(String username) {

    }

    public void validatePostId(String postId) {

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
