package com.order_manager.service;

import com.order_manager.dto.UserDto;
import com.order_manager.dto.UserInput;
import com.order_manager.entity.UserRole;
import com.order_manager.entity.DbUser;
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

    public UserDto createUser(UserInput input) {
        if (userRepository.findByName(input.name()).isPresent()) {
            throw new EntityExistsException("User with name " + input.name() + " already exists");
        }
        var dbUser = createDbUser(input);

        UserDTO response = userMapper.dbToDto(userRepository.save(user));
        return response;
        log.info("User with id #{} was created", userDto.id());
    }

    public List<UserDto> getAllUsers() {
        List<UserDto> userDto = userRepository.findAll().stream()
                .map(userMapper::dbToDto)
                .toList();

        log.info("Users was retrieved");
        return userDto;
    }

    public UserDto getUserById(@NonNull Long id) {
        var userDto = userRepository.findById(id)
                .map(userMapper::dbToDto)
                .orElseThrow(() -> new UserNotFoundException("User with id #" + id + " not found"));

        log.info("User with id #{} was retrieved", id);
        return userDto;
    }

    public UserDto updateUser(@NonNull Long id, UserInput input) {
        if (userRepository.findById(id).isEmpty()) {
            throw new UserNotFoundException("User with id #" + id + " not found");
        }
        var dbUser = createDbUser(id, input);
        var userDto = saveAndConvertToDto(dbUser);

        log.info("User with id #{} was updated", id);
        return userDto;
    }

    public void deleteUser(@NonNull Long id) {
        userRepository.findById(id)
                .ifPresentOrElse(userRepository::delete,
                        () -> { throw new UserNotFoundException("User with id #" + id + " not found"); });

        log.info("Product with id #{} was deleted", id);
    }

    private DbUser createDbUser(Long id, UserInput input) {
        return new DbUser(id, input.name(), passwordEncoder.encode(input.password()), UserRole.USER, input.email());
    }

    private UserDto saveAndConvertToDto(DbUser user) {
        return userMapper.dbToDto(userRepository.save(user));
    }
}
