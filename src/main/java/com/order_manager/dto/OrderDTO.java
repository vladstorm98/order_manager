package com.order_manager.dto;

import com.order_manager.entity.OrderStatus;
import com.order_manager.entity.ProductEntity;

import java.util.List;

public record OrderDTO(Long id, OrderStatus status, List<ProductEntity> products) implements DTO {
}
