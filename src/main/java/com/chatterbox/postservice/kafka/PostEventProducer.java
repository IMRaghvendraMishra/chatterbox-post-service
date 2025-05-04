package com.chatterbox.postservice.kafka;

import com.chatterbox.postservice.mapper.PostEventJsonMapper;
import com.chatterbox.postservice.model.Post;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@NoArgsConstructor
@AllArgsConstructor
public class PostEventProducer {

    @Autowired private KafkaTemplate<String, String> kafkaTemplate;
    @Autowired private PostEventJsonMapper mapper;

    @Value("${spring.kafka.post-service-topic-name}")
    private String postServiceTopicName;

    /**
     * Send post event to Kafka broker
     * @param postEvent
     */
    public void sendPostCreatedEvent(Post postEvent) {
        kafkaTemplate.send(postServiceTopicName, mapper.postEventToJson(postEvent));
    }
}
