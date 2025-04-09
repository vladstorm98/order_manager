package com.order_manager.service;

import com.order_manager.BaseTest;
import com.order_manager.dto.UserInput;
import com.order_manager.dto.UserDTO;
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
            GIVEN Input for creating a new user
            WHEN Creating a new user
            THEN Created user should be returned and saved to the database
            """)
    void shouldCreateUser() {
        // GIVEN
        var input = new UserInput(USER_NAME_NEW, USER_PASSWORD_NEW, USER_EMAIL_NEW);

        // WHEN
        var createdUser = userService.createUser(input);

        // THEN
        assertThat(createdUser).isNotNull()
                .satisfies(user -> {
                    assertThat(user.name()).isEqualTo(input.name());
                    assertThat(user.email()).isEqualTo(input.email());
        });

        assertThat(userRepository.findByName(USER_NAME_NEW)).isPresent();
    }

    @Nested
    @DisplayName("Tests for already created users")
    class CreatedUserTests {

        @Test
        @DisplayName("""
            GIVEN List of existing users in the database
            WHEN Fetching all users from the repository
            THEN All existing users from the database should be returned
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
            GIVEN Existing user
            WHEN Fetching the user by id
            THEN Created user should be returned
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
            GIVEN Existing user and input for updating the user
            WHEN Updating the user
            THEN User should be updated with the new values and saved to the database
            """)
        void shouldUpdateUser() {
            // GIVEN
            var oldUser = prepareUser();
            var input = new UserInput(USER_NAME_NEW, USER_PASSWORD_NEW, USER_EMAIL_NEW);

            // WHEN
            var updatedUser = userService.updateUser(oldUser.id(), input);

            // THEN
            assertThat(updatedUser).isNotNull()
                    .satisfies(user -> {
                            assertThat(user.id()).isEqualTo(oldUser.id());
                            assertThat(user.name()).isEqualTo(input.name());
                            assertThat(user.email()).isEqualTo(input.email());
            });

            assertThat(userRepository.findById(oldUser.id())).isPresent();
        }

        @Test
        @DisplayName("""
            GIVEN Existing user
            WHEN Deleting the user
            THEN User should be deleted from the database
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

    private UserDTO prepareUser(Long id, String name, String email) {
        return new UserDTO(id, name, email);
    }

    private UserDTO prepareUser() {
        return prepareUser(USER_ID_1, USER_NAME_1, USER_EMAIL_1);
    }

    private List<UserDTO> prepareUsers() {
        return List.of(
                prepareUser(USER_ID_1, USER_NAME_1, USER_EMAIL_1),
                prepareUser(USER_ID_2, USER_NAME_2, USER_EMAIL_2)
        );
    }
}
