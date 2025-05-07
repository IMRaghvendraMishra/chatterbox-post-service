package com.chatterbox.postservice.messaging;

import com.chatterbox.postservice.mapper.PostServiceJsonMapper;
import com.chatterbox.postservice.model.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostEventProducerTest {

    @InjectMocks
    private PostEventProducer postEventProducer;

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @Mock
    private PostServiceJsonMapper mapper;

    @Captor
    ArgumentCaptor<String> topicCaptor;

    @Captor
    ArgumentCaptor<String> messageCaptor;

    private final String mockTopic = "chatterbox-post-events";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // manually inject the @Value property
        postEventProducer = new PostEventProducer(kafkaTemplate, mapper, "postServiceTopicName");
        // Using reflection to inject the topic name as @Value does not work in unit tests
        try {
            var field = PostEventProducer.class.getDeclaredField("postServiceTopicName");
            field.setAccessible(true);
            field.set(postEventProducer, mockTopic);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void sendPostCreatedEvent_shouldSendMessageToKafka() {
        // given
        Post post = new Post();
        post.setPostId("p123");
        post.setUsername("john_doe");
        post.setContent("Test post content");

        String jsonMessage = "{\"postId\":\"p123\",\"username\":\"john_doe\",\"content\":\"Test post content\"}";
        when(mapper.postEventToJson(post)).thenReturn(jsonMessage);

        // when
        postEventProducer.sendPostCreatedEvent(post);

        // then
        verify(kafkaTemplate, times(1)).send(mockTopic, jsonMessage);
    }
}
