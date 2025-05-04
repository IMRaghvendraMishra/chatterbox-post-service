package com.chatterbox.postservice.controller;

import com.chatterbox.postservice.kafka.PostEventProducer;
import com.chatterbox.postservice.model.Post;
import com.chatterbox.postservice.service.PostService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@NoArgsConstructor
@AllArgsConstructor
public class PostController {

    @Autowired private PostService postService;
    @Autowired private PostEventProducer postEventProducer;

    @PostMapping
    public Post createPost(@RequestBody Post post) {
        var updatedPost = postService.createPost(post);
        postEventProducer.sendPostCreatedEvent(post); // Send event to Kafka topic
        return updatedPost;
    }

    @GetMapping("/user/{userId}")
    public List<Post> getPostsByUserId(@PathVariable String userId) {
        return postService.getPostsByUserId(userId);
    }

    @GetMapping("/post/{postId}")
    public Post getPostById(@PathVariable String postId) {
        return postService.getPostById(postId);
    }
}
