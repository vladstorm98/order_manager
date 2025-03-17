package com.order_manager;

import com.order_manager.dto.UserRequest;
import com.order_manager.dto.UserResponse;
import com.order_manager.entity.UserEntity;
import com.order_manager.mapper.UserMapper;
import com.order_manager.repository.UserRepository;
import com.order_manager.service.UserService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class UserServiceTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;

    @Test
    @Transactional
    void testGetAllUsers() {
        List<UserResponse> createdUsers = List.of(
                userService.createUser(new UserRequest("Iceberg", "Password")),
                userService.createUser(new UserRequest("Donald", "Password"))
        );
        List<UserResponse> fetchedUsers = userRepository.findAll().stream()
                .map(userMapper::toResponse)
                .collect(Collectors.toList());

        assertThat(fetchedUsers).isNotEmpty();
        assertThat(fetchedUsers).containsAll(createdUsers);
    }

    @Test
    @Transactional
    void testGetUserById() {
        UserResponse createdUser = userService.createUser(new UserRequest("Anfisa", "Password"));
        Long id = createdUser.id();

        UserResponse fetchedUser = userService.getUserById(id);

        assertThat(fetchedUser).isNotNull();
        assertThat(fetchedUser.id()).isEqualTo(createdUser.id());
        assertThat(fetchedUser.name()).isEqualTo(createdUser.name());
    }

    @Test
    @Transactional
    void testCreateUser() {
        UserRequest request = new UserRequest("Anton", "Password");

        UserResponse createdUser = userService.createUser(request);

        assertThat(createdUser).isNotNull();
        assertThat(createdUser.name()).isEqualTo(request.name());

        Optional<UserEntity> userFromDb = userRepository.findByName(request.name());
        assertThat(userFromDb).isPresent();
        assertThat(userFromDb.get().getName()).isEqualTo(request.name());
    }

    @Test
    @Transactional
    void testUpdateUser() {
        UserResponse user = userService.createUser(new UserRequest("OldName", "OldPassword"));
        Long id = user.id();

        UserRequest request = new UserRequest("UpdatedName", "UpdatedPassword");
        UserResponse updatedUser = userService.updateUser(id, request);

        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.name()).isEqualTo(request.name());

        Optional<UserEntity> userFromDb = userRepository.findById(id);
        assertThat(userFromDb).isPresent();
        assertThat(userFromDb.get().getName()).isEqualTo(request.name());
    }

    @Test
    @Transactional
    void testDeleteUser() {
        UserResponse user = userService.createUser(new UserRequest("Aurora", "Password"));
        Long id = user.id();

        userService.deleteUser(id);

        assertThat(userRepository.existsById(id)).isFalse();
    }
}
