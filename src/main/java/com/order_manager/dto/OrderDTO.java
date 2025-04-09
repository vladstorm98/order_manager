package com.order_manager.dto;

import com.order_manager.entity.DbProduct;
import com.order_manager.entity.OrderStatus;

import java.util.List;

public record OrderDTO(Long id, OrderStatus status, List<DbProduct> products) implements DTO {
}
