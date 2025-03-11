package com.order_manager.dto;

import com.order_manager.entity.OrderEntity;
import com.order_manager.entity.OrderStatus;
import com.order_manager.entity.ProductEntity;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class OrderResponse implements ResponseDTO {
    private Long id;
    private OrderStatus status;
    private List<Long> productIds;

    public OrderResponse(OrderEntity order) {
        this.id = order.getId();
        this.status = order.getStatus();
        this.productIds = order.getProducts().stream()
                .map(ProductEntity::getId)
                .collect(Collectors.toList());
    }
}