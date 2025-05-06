package com.chatterbox.postservice.repository;

import com.chatterbox.postservice.model.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Repository interface for managing {@link Post} entities in MongoDB.
 *
 * <p>
 * Extends {@link org.springframework.data.mongodb.repository.MongoRepository} to provide basic
 * CRUD operations for {@link Post} objects, with additional query methods tailored to the
 * ChatterBox platform's requirements.
 * </p>
 *
 * <p>
 * This repository provides methods for saving, retrieving, and deleting posts. It is
 * primarily used for interacting with the <strong>posts_collection</strong> in MongoDB.
 * </p>
 */

public interface PostRepository extends MongoRepository<Post, String>{
	List<Post> findByUsername(String username);
}