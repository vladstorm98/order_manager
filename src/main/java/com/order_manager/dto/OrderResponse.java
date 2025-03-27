package com.order_manager.dto;

import com.order_manager.entity.OrderStatus;
import java.util.List;

public record OrderResponse(long id, OrderStatus status, List<Long> productIds) implements ResponseDTO {
}
