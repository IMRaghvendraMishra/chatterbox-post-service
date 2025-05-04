package com.chatterbox.postservice.mapper;

import com.chatterbox.postservice.model.Post;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class PostEventJsonMapper {

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
}
