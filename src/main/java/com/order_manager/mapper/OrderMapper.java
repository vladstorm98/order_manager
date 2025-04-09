package com.order_manager.mapper;

import com.order_manager.dto.OrderDTO;
import com.order_manager.entity.DbOrder;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapper {
    OrderDTO dbToDto(DbOrder order);
}
