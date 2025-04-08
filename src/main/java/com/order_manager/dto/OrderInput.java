package com.order_manager.dto;

import com.order_manager.entity.ProductEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record OrderInput(
        @Schema(example = "[{\"id\": 1, \"name\": \"Product 1\", \"price\": 100.0}," +
                          " {\"id\": 2, \"name\": \"Product 2\", \"price\": 150.0}]")
        List<ProductEntity> products,

        @Schema(example = "1")
        Integer quantity
) {}
