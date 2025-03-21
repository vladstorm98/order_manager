package com.order_manager.dto;

import com.order_manager.entity.OrderEntity;
import com.order_manager.entity.OrderStatus;
import com.order_manager.entity.ProductEntity;
import java.util.List;

public record OrderResponse(
        Long id,
        OrderStatus status,
        List<Long> productIds
) implements ResponseDTO {

    public OrderResponse(OrderEntity order) {
        this(
                order.getId(),
                order.getStatus(),
                order.getProducts().stream()
                        .map(ProductEntity::getId)
                        .toList()
        );
    }
}
