package com.chatterbox.postservice.connector;

import com.chatterbox.postservice.exception.InvalidUserException;
import com.chatterbox.postservice.mapper.PostEventJsonMapper;
import com.chatterbox.postservice.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.netty.http.client.HttpClient;
import reactor.netty.http.client.HttpClientResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class HttpClientConnectorTest {

    @InjectMocks
    private HttpClientConnector httpClientConnector;

    @Mock
    private HttpClient httpClient;

    @Mock
    private HttpClientResponse httpClientResponse;

    @Mock
    private PostEventJsonMapper postEventJsonMapper;

    @Mock
    private User mockUser;

    @BeforeEach
    void setUp() {
        // Initialize mock objects if necessary
    }

    @Test
    void testGetUserByUsername_Success() {
        // Arrange:
//        when(httpClient.get()).thenReturn(httpClient);
//        when(httpClient.uri(any(String.class))).thenReturn(httpClient);
        String mockResponseJson = "{\"id\": \"12345\", \"userName\": \"john_doe\", \"firstName\": \"John\", \"lastName\": \"Doe\", \"email\": \"john.doe@example.com\"}";
//        when(httpClient.responseSingle(any(), any())).thenReturn(Mono.just(Tuple2.of(httpClientResponse, mockResponseJson)));

        // Mock response for status and user mapping
       // when(httpClientResponse.status()).thenReturn(mock(HttpClientResponse.class));
        when(postEventJsonMapper.jsonToUser(mockResponseJson)).thenReturn(mockUser);

        // Act: Call the method under test
        User result = httpClientConnector.getUserByUsername("john_doe");

        // Assert: Verify that the result is correct
        assertNotNull(result);
        verify(postEventJsonMapper, times(1)).jsonToUser(mockResponseJson);
    }

    @Test
    void testGetUserByUsername_Failure_InvalidUser() {
        // Arrange: Mock a failed response from the User Service (status code 400 or higher)
//        when(httpClient.get()).thenReturn(httpClient);
//        when(httpClient.uri(any(String.class))).thenReturn(httpClient);
//        when(httpClient.responseSingle(any(), any())).thenReturn(Mono.just(Tuple2.of(httpClientResponse, "")));
//        when(httpClientResponse.status()).thenReturn(mock(HttpClientResponse.class));

        // Simulate an error status (400 or above)
        when(httpClientResponse.status().code()).thenReturn(400);

        // Act and Assert: Verify that InvalidUserException is thrown
        InvalidUserException thrown = assertThrows(InvalidUserException.class, () -> {
            httpClientConnector.getUserByUsername("invalid_user");
        });

        assertEquals("User with username invalid_user does not exist", thrown.getMessage());
    }
}
