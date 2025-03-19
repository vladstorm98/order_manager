package com.order_manager.mapper;

import com.order_manager.dto.UserResponse;
import com.order_manager.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toResponse(UserEntity user);
}
