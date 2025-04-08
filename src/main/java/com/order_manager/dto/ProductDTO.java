package com.order_manager.dto;

import java.math.BigDecimal;

public record ProductDTO(Long id, String name, BigDecimal price) implements DTO {
}
