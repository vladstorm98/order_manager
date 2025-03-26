package com.order_manager.service;

import com.order_manager.BaseTest;
import com.order_manager.dto.UserRequest;
import com.order_manager.dto.UserResponse;
import com.order_manager.entity.UserEntity;
import com.order_manager.mapper.UserMapper;
import com.order_manager.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Integration tests for UserService")
public class UserServiceIntegrationTest extends BaseTest {

    private static final String NAME = "Name";
    private static final String UPDATED_NAME = "UpdatedName";
    private static final String PASSWORD = "Password";

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;

    @Test
    @DisplayName("""
            GIVEN a user request
            WHEN  creating a new user
            THEN  the created user should be returned and saved in the database
            """)
    void shouldCreateUser() {
        // GIVEN
        UserRequest request = new UserRequest(NAME, PASSWORD);

        // WHEN
        UserResponse createdUser = userService.createUser(request);

        // THEN
        assertThat(createdUser).isNotNull();
        assertThat(createdUser.name()).isEqualTo(request.name());

        Optional<UserEntity> userFromDb = userRepository.findByName(request.name());
        assertThat(userFromDb).isPresent().map(UserEntity::getName).get().isEqualTo(request.name());
    }

    @Nested
    @DisplayName("Tests for already created users")
    class CreatedUserTests {

        private UserResponse createdUser;

        @BeforeEach
        void beforeEach() {
            createdUser = createDefaultUser(userService);
        }

        @Test
        @DisplayName("""
            GIVEN a list of existing users in the database
            WHEN  fetching all users from the repository
            THEN  all existing users in the database should be returned
            """)
        void shouldGetAllUsers() {
            // GIVEN
            List<UserResponse> createdUsers = List.of(createdUser);

            // WHEN
            List<UserResponse> fetchedUsers = userRepository.findAll().stream()
                    .map(userMapper::toResponse)
                    .collect(Collectors.toList());

            // THEN
            assertThat(fetchedUsers).isNotEmpty();
            assertThat(fetchedUsers).containsAll(createdUsers);
        }

        @Test
        @DisplayName("""
            GIVEN an existing user
            WHEN  fetching the user by id
            THEN  the created user should be returned
            """)
        void shouldGetUserById() {
            // GIVEN
            Long id = createdUser.id();

            // WHEN
            UserResponse fetchedUser = userService.getUserById(id);

            // THEN
            assertThat(fetchedUser).isNotNull().satisfies( user -> {
                assertThat(user.id()).isEqualTo(createdUser.id());
                assertThat(user.name()).isEqualTo(createdUser.name());
            });
        }

        @Test
        @DisplayName("""
            GIVEN an existing user and a user request
            WHEN  updating the user
            THEN  the updated user should be updated with new values and saved in the database
            """)
        void shouldUpdateUser() {
            // GIVEN
            Long id = createdUser.id();
            UserRequest request = new UserRequest(UPDATED_NAME, PASSWORD);

            // WHEN
            UserResponse updatedUser = userService.updateUser(id, request);

            // THEN
            assertThat(updatedUser).isNotNull();
            assertThat(updatedUser.name()).isEqualTo(request.name());

            Optional<UserEntity> userFromDb = userRepository.findById(id);
            assertThat(userFromDb).isPresent().map(UserEntity::getName).get().isEqualTo(request.name());
        }

        @Test
        @DisplayName("""
            GIVEN an existing user
            WHEN  deleting the user
            THEN  the user should be deleted from the database
            """)
        void shouldDeleteUser() {
            // GIVEN
            Long id = createdUser.id();

            // WHEN
            userService.deleteUser(id);

            // THEN
            assertThat(userRepository.existsById(id)).isFalse();
        }
    }

    private static UserResponse createDefaultUser(UserService userService) {
        return userService.createUser(new UserRequest(NAME, PASSWORD));
    }
}
