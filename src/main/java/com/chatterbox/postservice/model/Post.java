package com.chatterbox.postservice.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Document(collection = "posts_collection")
public class Post {
	
	@Id
    private String postId;
    
    private String username;
    
    private String content;
    
    private Instant timestamp;
}
