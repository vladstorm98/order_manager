package com.order_manager.service;

import com.order_manager.BaseTest;
import com.order_manager.dto.UserRequest;
import com.order_manager.dto.UserResponse;
import com.order_manager.exception.UserNotFoundException;
import com.order_manager.mapper.UserMapper;
import com.order_manager.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Integration tests for UserService")
public class UserServiceIntegrationTest extends BaseTest {

    private static final Long USER_ID = 1L;
    private static final String NEW_NAME = "NewName";
    private static final String NEW_PASSWORD = "Password";
    private static final String NEW_EMAIL = "example@gmail.com";

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
        var request = new UserRequest(NEW_NAME, NEW_PASSWORD, NEW_EMAIL);

        // WHEN
        var createdUser = userService.createUser(request);

        // THEN
        assertThat(createdUser).isNotNull()
                .satisfies(user -> {
                    assertThat(user.name()).isEqualTo(NEW_NAME);
                    assertThat(user.email()).isEqualTo(NEW_EMAIL);
        });

        assertThat(userRepository.findByName(NEW_NAME)).isPresent();
    }

    @Nested
    @DisplayName("Tests for already created users")
    class CreatedUserTests {

        private UserResponse createdUser;

        @BeforeEach
        void beforeEach() {
            createdUser = getUserFromDb();
        }

        @Test
        @DisplayName("""
            GIVEN a list of existing users in the database
            WHEN  fetching all users from the repository
            THEN  all existing users in the database should be returned
            """)
        void shouldGetAllUsers() {
            // GIVEN
            var createdUsers = getAllUsersFromDb();

            // WHEN
            var fetchedUsers = userService.getAllUsers();

            // THEN
            assertThat(fetchedUsers).isNotNull()
                    .hasSize(createdUsers.size())
                    .containsExactlyInAnyOrderElementsOf(createdUsers);

            assertThat(fetchedUsers.getFirst())
                    .satisfies(user -> {
                            assertThat(user.id()).isEqualTo(createdUser.id());
                            assertThat(user.name()).isEqualTo(createdUser.name());
                            assertThat(user.email()).isEqualTo(createdUser.email());
            });
        }

        @Test
        @DisplayName("""
            GIVEN an existing user
            WHEN  fetching the user by id
            THEN  the created user should be returned
            """)
        void shouldGetUserById() {
            // GIVEN
            var id = createdUser.id();

            // WHEN
            var fetchedUser = userService.getUserById(id);

            // THEN
            assertThat(fetchedUser).isNotNull()
                    .satisfies( user -> {
                            assertThat(user.id()).isEqualTo(createdUser.id());
                            assertThat(user.name()).isEqualTo(createdUser.name());
                            assertThat(user.email()).isEqualTo(createdUser.email());
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
            var id = createdUser.id();
            var request = new UserRequest(NEW_NAME, NEW_PASSWORD, NEW_EMAIL);

            // WHEN
            var updatedUser = userService.updateUser(id, request);

            // THEN
            assertThat(updatedUser).isNotNull()
                    .satisfies(user -> {
                            assertThat(user.id()).isEqualTo(createdUser.id());
                            assertThat(user.name()).isEqualTo(NEW_NAME);
                            assertThat(user.email()).isEqualTo(NEW_EMAIL);
            });

            assertThat(userRepository.findById(id)).isPresent();
        }

        @Test
        @DisplayName("""
            GIVEN an existing user
            WHEN  deleting the user
            THEN  the user should be deleted from the database
            """)
        void shouldDeleteUser() {
            // GIVEN
            var id = createdUser.id();

            // WHEN
            userService.deleteUser(id);

            // THEN
            assertThat(userRepository.existsById(id)).isFalse();
        }
    }

    private UserResponse getUserFromDb() {
        return userRepository.findById(USER_ID)
                .map(userMapper::toResponse)
                .orElseThrow(() -> new UserNotFoundException("Product with ID " + USER_ID + " not found"));
    }

    private List<UserResponse> getAllUsersFromDb() {
        return userRepository.findAll().stream()
                .map(userMapper::toResponse)
                .toList();
    }
}
