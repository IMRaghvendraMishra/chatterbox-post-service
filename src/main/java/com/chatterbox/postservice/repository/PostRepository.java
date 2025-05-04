package com.chatterbox.postservice.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.chatterbox.postservice.model.Post;

public interface PostRepository extends MongoRepository<Post, String>{
	
	List<Post> findByUserId(String userId);
}