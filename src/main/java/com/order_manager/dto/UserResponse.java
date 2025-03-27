package com.order_manager.dto;

public record UserResponse(long id, String name, String email) implements ResponseDTO{
}

