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

    private static final Long FIRST_USER_ID = 1L;
    private static final Long SECOND_USER_ID = 2L;
    private static final String FIRST_USER_NAME = "Name_1";
    private static final String FIRST_USER_EMAIL = "email_1@gmail.com";
    private static final String SECOND_USER_NAME = "Name_2";
    private static final String SECOND_USER_EMAIL = "email_2@gmail.com";
    private static final String NEW_USER_NAME = "NewName";
    private static final String NEW_USER_PASSWORD = "NewPassword";
    private static final String NEW_USER_EMAIL = "new_email@gmail.com";

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
        var request = new UserRequest(NEW_USER_NAME, NEW_USER_PASSWORD, NEW_USER_EMAIL);

        // WHEN
        var createdUser = userService.createUser(request);

        // THEN
        assertThat(createdUser).isNotNull()
                .satisfies(user -> {
                    assertThat(user.name()).isEqualTo(request.name());
                    assertThat(user.email()).isEqualTo(request.email());
        });

        assertThat(userRepository.findByName(NEW_USER_NAME)).isPresent();
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
            var allExpectedUsers = getAllUsers();
            var expectedUser = getFirstUser();

            // WHEN
            var actualUsers = userService.getAllUsers();

            // THEN
            assertThat(actualUsers).isNotNull()
                    .hasSize(allExpectedUsers.size())
                    .containsExactlyInAnyOrderElementsOf(allExpectedUsers);

            assertThat(actualUsers.getFirst())
                    .satisfies(user -> {
                            assertThat(user.id()).isEqualTo(expectedUser.id());
                            assertThat(user.name()).isEqualTo(expectedUser.name());
                            assertThat(user.email()).isEqualTo(expectedUser.email());
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
            var createdUser = getFirstUser();

            // WHEN
            var actualUsers = userService.getUserById(createdUser.id());

            // THEN
            assertThat(actualUsers).isNotNull()
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
            var oldUser = getFirstUser();
            var request = new UserRequest(NEW_USER_NAME, NEW_USER_PASSWORD, NEW_USER_EMAIL);

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
            var user = getFirstUser();

            // WHEN
            userService.deleteUser(user.id());

            // THEN
            assertThat(userRepository.existsById(user.id())).isFalse();
        }
    }

    private UserResponse getFirstUser() {
        return new UserResponse(FIRST_USER_ID, FIRST_USER_NAME, FIRST_USER_EMAIL);
    }

    private List<UserResponse> getAllUsers() {
        return List.of(
                new UserResponse(FIRST_USER_ID, FIRST_USER_NAME, FIRST_USER_EMAIL),
                new UserResponse(SECOND_USER_ID, SECOND_USER_NAME, SECOND_USER_EMAIL)
        );
    }
}
