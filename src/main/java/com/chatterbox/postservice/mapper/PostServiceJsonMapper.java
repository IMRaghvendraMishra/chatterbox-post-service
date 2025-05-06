package com.chatterbox.postservice.mapper;

import com.chatterbox.postservice.model.Post;
import com.chatterbox.postservice.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Utility service for mapping between domain objects and their JSON representations
 * using Jackson's {@link ObjectMapper}.
 *
 * <p>
 * Responsible for:
 * <ul>
 *     <li>Serializing {@link Post} objects into JSON strings for Kafka publishing.</li>
 *     <li>Deserializing JSON responses from User Service into {@link User} objects.</li>
 * </ul>
 *
 * <p>
 * Logs errors for mapping failures without throwing exceptions, returning empty or null
 * values instead to maintain service flow.
 * </p>
 */
@Service
@Log4j2
public class PostServiceJsonMapper {

    @Autowired ObjectMapper mapper;

    public String postEventToJson(Post postEvent) {
        var json = "";
        try {
            json = mapper.writeValueAsString(postEvent);
        } catch (JsonProcessingException e) {
            log.error("Unable to map PostEvent object to JSON");
        }
        return json;
    }

    public User jsonToUser(String json) {
        try {
            return mapper.readValue(json, User.class);
        } catch (JsonProcessingException e) {
            log.error("Unable to parse JSON to User object", e);
            return null;
        }
    }
}
