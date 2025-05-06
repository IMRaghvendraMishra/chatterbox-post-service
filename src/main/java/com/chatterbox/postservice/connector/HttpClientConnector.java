package com.chatterbox.postservice.connector;

import com.chatterbox.postservice.exception.InvalidUserException;
import com.chatterbox.postservice.mapper.PostEventJsonMapper;
import com.chatterbox.postservice.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.http.client.HttpClientResponse;
import reactor.util.function.Tuple2;

import java.time.Duration;

@Component
@RequiredArgsConstructor
@Log4j2
public class HttpClientConnector {

    private static final String USER_SERVICE_BASE_URL = "http://localhost:9091/api/users/username/";

    private final PostEventJsonMapper mapper;

    public User getUserByUsername(String username) {
        String uri = USER_SERVICE_BASE_URL + username;

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
