package com.order_manager.mapper;

import com.order_manager.dto.OrderResponse;
import com.order_manager.entity.OrderEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderResponse toResponse(OrderEntity order);
}
