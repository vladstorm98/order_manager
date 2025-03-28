package com.order_manager.dto;

public record UserResponse(Long id, String name, String email) implements ResponseDTO{
}

