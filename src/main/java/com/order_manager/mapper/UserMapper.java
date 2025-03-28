package com.order_manager.mapper;

import com.order_manager.dto.UserResponse;
import com.order_manager.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    UserResponse toResponse(UserEntity user);
}
