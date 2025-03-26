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
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserResponse getUserById(Long id) {
        if (userRepository.findById(id).isEmpty()) {
            throw new UserNotFoundException("User with id #" + id + " not found");
        }

        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        UserResponse response = userMapper.toResponse(user);

        log.info("User with id #{} was retrieved", id);
        return response;
    }

    public UserResponse createUser(UserRequest request) {
        if (userRepository.findByName(request.name()).isPresent()) {
            throw new EntityExistsException("User with name " + request.name() + " already exists");
        }

        UserEntity user = new UserEntity(
                request.name(), passwordEncoder.encode(request.password()), UserRole.USER, request.email());
        
        UserResponse response = userMapper.toResponse(userRepository.save(user));

        log.info("User with id #{} was created", response.id());
        return response;
    }

    public UserResponse updateUser(long id, UserRequest request) {
        if (userRepository.findById(id).isEmpty()) {
            throw new UserNotFoundException("User with id #" + id + " not found");
        }

        UserEntity user = new UserEntity(
                id, request.name(), passwordEncoder.encode(request.password()), UserRole.USER, request.email());

        UserResponse response = userMapper.toResponse(userRepository.save(user));

        log.info("User with id #{} was updated", id);
        return response;
    }

    public void deleteUser(long id) {
        if (userRepository.findById(id).isEmpty()) {
            throw new UserNotFoundException("User with id #" + id + " not found");
        }

        userRepository.deleteById(id);
    }
}
