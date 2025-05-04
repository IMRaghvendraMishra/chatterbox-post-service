package com.chatterbox.postservice.service;

import com.chatterbox.postservice.model.Post;
import com.chatterbox.postservice.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.List;

@Service
public class PostService {
	
	@Autowired
    private PostRepository postRepository;

    public Post createPost(Post post) {
        post.setTimestamp(Instant.now());
        postRepository.save(post);
        return post;
    }

    public List<Post> getPostsByUserId(String userId) {
        
    	return postRepository.findByUserId(userId);
    }

    public Post getPostById(String postId) throws Exception {
        return postRepository.findById(postId).orElseThrow(() -> new Exception("Post not found"));
    }
}
