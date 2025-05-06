package com.chatterbox.postservice.connector;

import com.chatterbox.postservice.exception.InvalidUserException;
import com.chatterbox.postservice.mapper.PostServiceJsonMapper;
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
 * Connector component responsible for communicating with the User Service to fetch user details
 * for the ChatterBox platform.
 *
 * <p>
 * Uses Reactor Netty's {@link HttpClient} to perform a non-blocking HTTP GET request, but synchronously
 * waits for the response using {@code block()} with a timeout to remain compatible with the blocking
 * flow of the Post Service.
 * </p>
 *
 * <p><strong>Responsibilities:</strong></p>
 * <ul>
 *   <li>Construct the complete URI using a configurable base path and the provided username.</li>
 *   <li>Execute the HTTP GET call to retrieve user information.</li>
 *   <li>Convert the JSON response into a {@link User} object using {@link PostServiceJsonMapper}.</li>
 *   <li>Handle HTTP errors or null responses by logging and throwing {@link InvalidUserException}.</li>
 * </ul>
 *
 * <p>
 * The endpoint URL is configured via the <code>post.connector.user-service.get-by-username-endpoint</code>
 * property in the application configuration.
 * </p>
 */

@Component
@NoArgsConstructor
@Log4j2
public class HttpClientConnector {

    @Value("${post.connector.user-service.get-by-username-endpoint}")
    private String getByUsernameAPIEndpoint;

    @Autowired private PostServiceJsonMapper mapper;

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
