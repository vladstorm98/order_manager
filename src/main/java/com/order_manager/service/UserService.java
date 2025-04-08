package com.order_manager.service;

import com.order_manager.dto.UserDTO;
import com.order_manager.dto.UserInput;
import com.order_manager.entity.UserRole;
import com.order_manager.entity.UserEntity;
import com.order_manager.exception.UserNotFoundException;
import com.order_manager.mapper.UserMapper;
import com.order_manager.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserDTO createUser(UserInput input) {
        if (userRepository.findByName(input.name()).isPresent()) {
            throw new EntityExistsException("User with name " + input.name() + " already exists");
        }

        UserEntity user = new UserEntity(
                input.name(), passwordEncoder.encode(input.password()), UserRole.USER, input.email());

        UserDTO response = userMapper.dbToDto(userRepository.save(user));

        log.info("User with id #{} was created", response.id());
        return response;
    }

    public List<UserDTO> getAllUsers() {
        List<UserDTO> response = userRepository.findAll().stream()
                .map(userMapper::dbToDto)
                .toList();

        log.info("Users was retrieved");
        return response;
    }

    public UserDTO getUserById(@NonNull Long id) {
        UserDTO response = userRepository.findById(id)
                .map(userMapper::dbToDto)
                .orElseThrow(() -> new UserNotFoundException("User with id #" + id + " not found"));

        log.info("User with id #{} was retrieved", id);
        return response;
    }

    public UserDTO updateUser(@NonNull Long id, UserInput input) {
        if (userRepository.findById(id).isEmpty()) {
            throw new UserNotFoundException("User with id #" + id + " not found");
        }

        UserEntity user = new UserEntity(
                id, input.name(), passwordEncoder.encode(input.password()), UserRole.USER, input.email());

        UserDTO response = userMapper.dbToDto(userRepository.save(user));

        log.info("User with id #{} was updated", id);
        return response;
    }

    public void deleteUser(@NonNull Long id) {
        userRepository.findById(id)
                .ifPresentOrElse(userRepository::delete,
                        () -> { throw new UserNotFoundException("User with id #" + id + " not found"); });

        log.info("Product with id #{} was deleted", id);
    }
}
