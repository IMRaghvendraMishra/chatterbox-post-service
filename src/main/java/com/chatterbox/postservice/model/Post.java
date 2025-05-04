package com.chatterbox.postservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "posts_collection")
public class Post {
	
	@Id
    private String postId;
    
    private String userId;
    
    private String content;
    
    private Instant timestamp;
}
