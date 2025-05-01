package com.chatterbox.postservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    private String postId;
    private String userId;
    private String content;
    private Instant timestamp;
}
