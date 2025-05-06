package com.chatterbox.postservice.kafka;

import com.chatterbox.postservice.mapper.PostServiceJsonMapper;
import com.chatterbox.postservice.model.Post;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * Kafka producer service for publishing post creation events to the configured topic
 * in the ChatterBox platform.
 *
 * <p>
 * Converts {@link com.chatterbox.postservice.model.Post} objects to JSON strings using
 * {@link PostServiceJsonMapper} and sends them to the Kafka
 * topic defined by the <code>spring.kafka.post-events-topic-name</code> property.
 * </p>
 *
 * <p>
 * This service enables asynchronous communication between microservices by emitting events
 * that can be consumed by other components (e.g., NotificationService).
 * </p>
 */
@Service
@Log4j2
@NoArgsConstructor
@AllArgsConstructor
public class PostEventProducer {

    @Autowired private KafkaTemplate<String, String> kafkaTemplate;
    @Autowired private PostServiceJsonMapper mapper;

    @Value("${spring.kafka.post-events-topic-name}")
    String postServiceTopicName;

    /**
     * Send post event to Kafka broker
     * @param postEvent
     */
    public void sendPostCreatedEvent(Post postEvent) {
        kafkaTemplate.send(postServiceTopicName, mapper.postEventToJson(postEvent));
    }
}
