package com.order_manager.mapper;

import com.order_manager.dto.UserDto;
import com.order_manager.entity.DbUser;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    UserDto dbToDto(DbUser user);
}
