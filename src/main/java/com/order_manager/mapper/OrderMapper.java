package com.order_manager.mapper;

import com.order_manager.dto.OrderResponse;
import com.order_manager.entity.OrderEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapper {
    OrderResponse toResponse(OrderEntity order);
}
