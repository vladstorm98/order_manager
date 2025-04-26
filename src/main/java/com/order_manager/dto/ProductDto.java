package com.order_manager.dto;

import java.math.BigDecimal;

public record ProductDto(Long id, String name, BigDecimal price) implements Dto {}
