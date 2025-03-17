package com.order_manager.dto;

import com.order_manager.entity.UserEntity;

public record UserResponse(Long id, String name, String email) implements ResponseDTO{

    public UserResponse(UserEntity user) {
        this(user.getId(), user.getName(), user.getEmail());
    }
}

