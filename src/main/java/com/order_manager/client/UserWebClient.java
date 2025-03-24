package com.order_manager.client;

import com.order_manager.dto.UserRequest;
import com.order_manager.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class UserWebClient implements UserUrlTemplate {

    private final WebClient webClient;

    public UserResponse fetchUser(Long userId) {
        return webClient.get()
                .uri(USER_BY_ID, userId)
                .retrieve()
                .bodyToMono(UserResponse.class)
                .block();
    }

    public UserResponse createUser(UserRequest userRequest) {
        return webClient.post()
                .uri(USERS)
                .bodyValue(userRequest)
                .retrieve()
                .bodyToMono(UserResponse.class)
                .block();
    }

    public UserResponse updateUser(Long userId, UserRequest userRequest) {
        return webClient.put()
                .uri(USER_BY_ID, userId)
                .bodyValue(userRequest)
                .retrieve()
                .bodyToMono(UserResponse.class)
                .block();
    }

    public void deleteUser(Long userId) {
        webClient.delete()
                .uri(USER_BY_ID, userId)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }
}
