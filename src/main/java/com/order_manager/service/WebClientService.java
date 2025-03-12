package com.order_manager.service;

import com.order_manager.dto.UserRequest;
import com.order_manager.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class WebClientService {

    private final WebClient webClient;

    public UserResponse fetchUser(Long userId) {
        return webClient.get()
                .uri("/users/{id}", userId)
                .retrieve()
                .bodyToMono(UserResponse.class)
                .block();
    }

    public UserResponse createUser(UserRequest userRequest) {
        return webClient.post()
                .uri("/users")
                .bodyValue(userRequest)
                .retrieve()
                .bodyToMono(UserResponse.class)
		        .block();
    }

    public UserResponse updateUser(Long userId, UserRequest userRequest) {
        return webClient.put()
                .uri("/users/{id}", userId)
                .bodyValue(userRequest)
                .retrieve()
                .bodyToMono(UserResponse.class)
                .block();
    }

    public void deleteUser(Long userId) {
        webClient.delete()
                .uri("/users/{id}", userId)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }
}

