package com.order_manager.dto;

public record UserDTO(Long id, String name, String email) implements DTO {
}

