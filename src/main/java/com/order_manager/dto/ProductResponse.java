package com.order_manager.dto;

import com.order_manager.entity.ProductEntity;

public record ProductResponse(
        long id,
        String name,
        double price
) implements ResponseDTO {

    public ProductResponse(ProductEntity product) {
        this(product.getId(), product.getName(), product.getPrice());
    }
}
