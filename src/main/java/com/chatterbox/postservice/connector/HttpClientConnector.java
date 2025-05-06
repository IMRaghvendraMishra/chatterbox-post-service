package com.chatterbox.postservice.connector;

import com.chatterbox.postservice.exception.InvalidUserException;
import com.chatterbox.postservice.mapper.PostEventJsonMapper;
import com.chatterbox.postservice.model.User;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.http.client.HttpClientResponse;
import reactor.util.function.Tuple2;

import java.time.Duration;

/**
 * Connector component responsible for making HTTP calls to the User Service to fetch user details.
 * <p>
 * This class uses Reactor Netty's {@link HttpClient} to perform non-blocking HTTP requests, but
 * blocks the result synchronously using {@code block()} with a timeout to integrate with
 * the synchronous Post Service logic.
 * <p>
 * Key Responsibilities:
 * <ul>
 *   <li>Build the full URI using a configurable base endpoint and the provided username</li>
 *   <li>Perform GET request to retrieve user details from the User Service</li>
 *   <li>Parse JSON response into a {@link User} object using {@link PostEventJsonMapper}</li>
 *   <li>Log errors and throw {@link com.chatterbox.postservice.exception.InvalidUserException}
 *       for non-2xx HTTP responses or null responses</li>
 * </ul>
 * <p>
 * The endpoint URI is configured using the property {@code post.connector.user-service.get-by-username-endpoint}
 * in the application configuration file.
 */
@Component
@NoArgsConstructor
@Log4j2
public class HttpClientConnector {

    @Value("${post.connector.user-service.get-by-username-endpoint}")
    private String getByUsernameAPIEndpoint;

    @Autowired private PostEventJsonMapper mapper;

    public User getUserByUsername(String username) {
        String uri = getByUsernameAPIEndpoint + username;

        Tuple2<HttpClientResponse, String> response = HttpClient.create()
                .headers(h -> h.add("Accept", "application/json"))
                .get()
                .uri(uri)
                .responseSingle((res, content) -> Mono.just(res).zipWith(content.asString()))
                .block(Duration.ofSeconds(30));

        if (response == null || response.getT1().status().code() >= 400) {
            String errorMsg = "Failed to fetch user: " + username;
            log.error(errorMsg);
            throw new InvalidUserException("User with username " + username + " does not exist");
        }

        return mapper.jsonToUser(response.getT2());
    }
}
