package com.chatterbox.postservice.service;

import com.chatterbox.postservice.model.Post;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {

    public Post createPost(Post post) {
        post.setPostId("mock-post-id");
        post.setTimestamp(Instant.now());
        return post;
    }

    public List<Post> getPostsByUserId(String userId) {
        Post post1 = new Post("post-id1", userId, "content", Instant.now());
        Post post2 = new Post("post-id2", userId, "content", Instant.now());
        Post post3 = new Post("post-id3", userId, "content", Instant.now());
        List<Post> postList = new ArrayList<>();
        postList.add(post1);
        postList.add(post2);
        postList.add(post3);
        return postList; // mock
    }

    public Post getPostById(String postId) {
        return new Post(postId, "mock-user", "Mock content", Instant.now());
    }
}
