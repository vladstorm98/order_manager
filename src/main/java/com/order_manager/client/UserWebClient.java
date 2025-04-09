package com.order_manager.client;

import com.order_manager.dto.UserDTO;
import com.order_manager.dto.UserInput;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class UserWebClient implements UserUrlTemplate {

    private final WebClient webClient;

    public UserDTO fetchUser(Long userId) {
        return webClient.get()
                .uri(USER_BY_ID, userId)
                .retrieve()
                .bodyToMono(UserDTO.class)
                .block();
    }

    public UserDTO createUser(UserInput userRequest) {
        return webClient.post()
                .uri(USERS)
                .bodyValue(userRequest)
                .retrieve()
                .bodyToMono(UserDTO.class)
                .block();
    }

    public UserDTO updateUser(Long userId, UserInput userRequest) {
        return webClient.put()
                .uri(USER_BY_ID, userId)
                .bodyValue(userRequest)
                .retrieve()
                .bodyToMono(UserDTO.class)
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
