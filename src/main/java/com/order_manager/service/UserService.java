package com.order_manager.service;

import com.order_manager.dto.UserRequest;
import com.order_manager.dto.UserResponse;
import com.order_manager.entity.UserRole;
import com.order_manager.entity.UserEntity;
import com.order_manager.exception.UserNotFoundException;
import com.order_manager.mapper.UserMapper;
import com.order_manager.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserResponse getUserById(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return userMapper.toResponse(user);
    }

    public UserResponse createUser(UserRequest request) {
        if (userRepository.findByName(request.name()).isPresent()) {
            throw new EntityExistsException("User with name " + request.name() + " already exists");
        }

        UserEntity user = new UserEntity(
                request.name(), passwordEncoder.encode(request.password()), UserRole.USER, request.email());
        return userMapper.toResponse(userRepository.save(user));
    }

    public UserResponse updateUser(long id, UserRequest request) {
        if (userRepository.findById(id).isEmpty()) {
            throw new UserNotFoundException("User not found");
        }

        UserEntity user = new UserEntity(
                id, request.name(), passwordEncoder.encode(request.password()), UserRole.USER, request.email());
        return userMapper.toResponse(userRepository.save(user));
    }

    public void deleteUser(long userId) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new UserNotFoundException("User not found");
        }

        userRepository.deleteById(userId);
    }
}
