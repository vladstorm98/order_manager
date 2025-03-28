package com.order_manager.service;

import com.order_manager.BaseTest;
import com.order_manager.dto.UserRequest;
import com.order_manager.dto.UserResponse;
import com.order_manager.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Integration tests for UserService")
public class UserServiceIntegrationTest extends BaseTest {

    private static final Long USER_ID_1 = 1L;
    private static final Long USER_ID_2 = 2L;
    private static final String USER_NAME_1 = "Name_1";
    private static final String USER_NAME_2 = "Name_2";
    private static final String USER_NAME_NEW = "NewName";
    private static final String USER_PASSWORD_NEW = "NewPassword";
    private static final String USER_EMAIL_1 = "email_1@example.com";
    private static final String USER_EMAIL_2 = "email_2@example.com";
    private static final String USER_EMAIL_NEW = "email_new@example.com";

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @Test
    @DisplayName("""
            GIVEN a user request
            WHEN  creating a new user
            THEN  the created user should be returned and saved in the database
            """)
    void shouldCreateUser() {
        // GIVEN
        var request = new UserRequest(USER_NAME_NEW, USER_PASSWORD_NEW, USER_EMAIL_NEW);

        // WHEN
        var createdUser = userService.createUser(request);

        // THEN
        assertThat(createdUser).isNotNull()
                .satisfies(user -> {
                    assertThat(user.name()).isEqualTo(request.name());
                    assertThat(user.email()).isEqualTo(request.email());
        });

        assertThat(userRepository.findByName(USER_NAME_NEW)).isPresent();
    }

    @Nested
    @DisplayName("Tests for already created users")
    class CreatedUserTests {

        @Test
        @DisplayName("""
            GIVEN a list of existing users in the database
            WHEN  fetching all users from the repository
            THEN  all existing users in the database should be returned
            """)
        void shouldGetAllUsers() {
            // GIVEN
            var expectedUsers = prepareUsers();

            // WHEN
            var actualUsers = userService.getAllUsers();

            // THEN
            assertThat(actualUsers).isNotNull()
                    .hasSize(expectedUsers.size())
                    .containsExactlyInAnyOrderElementsOf(expectedUsers);

            assertThat(actualUsers.getFirst())
                    .satisfies(user -> {
                            assertThat(user.id()).isEqualTo(expectedUsers.getFirst().id());
                            assertThat(user.name()).isEqualTo(expectedUsers.getFirst().name());
                            assertThat(user.email()).isEqualTo(expectedUsers.getFirst().email());
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
            var expectedUser = prepareUser();

            // WHEN
            var actualUser = userService.getUserById(expectedUser.id());

            // THEN
            assertThat(actualUser).isNotNull()
                    .satisfies( user -> {
                            assertThat(user.id()).isEqualTo(expectedUser.id());
                            assertThat(user.name()).isEqualTo(expectedUser.name());
                            assertThat(user.email()).isEqualTo(expectedUser.email());
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
            var oldUser = prepareUser();
            var request = new UserRequest(USER_NAME_NEW, USER_PASSWORD_NEW, USER_EMAIL_NEW);

            // WHEN
            var updatedUser = userService.updateUser(oldUser.id(), request);

            // THEN
            assertThat(updatedUser).isNotNull()
                    .satisfies(user -> {
                            assertThat(user.id()).isEqualTo(oldUser.id());
                            assertThat(user.name()).isEqualTo(request.name());
                            assertThat(user.email()).isEqualTo(request.email());
            });

            assertThat(userRepository.findById(oldUser.id())).isPresent();
        }

        @Test
        @DisplayName("""
            GIVEN an existing user
            WHEN  deleting the user
            THEN  the user should be deleted from the database
            """)
        void shouldDeleteUser() {
            // GIVEN
            var user = prepareUser();

            // WHEN
            userService.deleteUser(user.id());

            // THEN
            assertThat(userRepository.existsById(user.id())).isFalse();
        }
    }

    private UserResponse prepareUser(Long id, String name, String email) {
        return new UserResponse(id, name, email);
    }

    private UserResponse prepareUser() {
        return prepareUser(USER_ID_1, USER_NAME_1, USER_EMAIL_1);
    }

    private List<UserResponse> prepareUsers() {
        return List.of(
                prepareUser(USER_ID_1, USER_NAME_1, USER_EMAIL_1),
                prepareUser(USER_ID_2, USER_NAME_2, USER_EMAIL_2)
        );
    }
}
