package com.order_manager.dto;

public record UserDto(Long id, String name, String email) implements Dto {}

