package com.chatterbox.postservice.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

/**
 * Domain model representing a post in the ChatterBox platform.
 *
 * <p>
 * Each post is stored in the MongoDB collection named <strong>posts_collection</strong>
 * and includes metadata such as the author's username, the post content, and the creation timestamp.
 * </p>
 *
 * <p><strong>Fields:</strong></p>
 * <ul>
 *   <li><code>postId</code> — Unique identifier for the post (MongoDB @Id).</li>
 *   <li><code>username</code> — Username of the post author.</li>
 *   <li><code>content</code> — Text content of the post.</li>
 *   <li><code>timestamp</code> — Time when the post was created.</li>
 * </ul>
 *
 * <p>
 * Annotated with Lombok's {@link lombok.Data} to generate boilerplate code, and
 * Spring Data's {@link org.springframework.data.mongodb.core.mapping.Document} for MongoDB persistence.
 * </p>
 */
@Data
@Document(collection = "posts_collection")
public class Post {
	
	@Id
    private String postId;
    
    private String username;
    
    private String content;
    
    private Instant timestamp;
}
