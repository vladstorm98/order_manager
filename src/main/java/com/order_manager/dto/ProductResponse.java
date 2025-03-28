package com.order_manager.dto;

import java.math.BigDecimal;

public record ProductResponse(Long id, String name, BigDecimal price) implements ResponseDTO {
}
