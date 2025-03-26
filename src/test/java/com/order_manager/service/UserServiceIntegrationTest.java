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

    private static final String NAME_1 = "Name_1";
    private static final String NAME_2 = "Name_2";
    private static final String UPDATED_NAME = "UpdatedName";
    private static final String PASSWORD = "Password";
    private static final String EMAIL = "email@gmail.com";

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
        var request = new UserRequest(NAME_1, PASSWORD, EMAIL);

        // WHEN
        var createdUser = userService.createUser(request);

        // THEN
        assertThat(createdUser).isNotNull()
                .satisfies(user -> {
                    assertThat(user.name()).isEqualTo(NAME_1);
                    assertThat(user.email()).isEqualTo(EMAIL);
        });

        assertThat(userRepository.findByName(NAME_1)).isPresent();
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
            var createdUsers = createListOfUsers(userService);

            // WHEN
            var fetchedUsers = userService.getAllUsers();

            // THEN
            assertThat(fetchedUsers).isNotNull()
                    .hasSize(createdUsers.size())
                    .containsExactlyInAnyOrderElementsOf(createdUsers);

            assertThat(fetchedUsers.getFirst())
                    .satisfies(user -> {
                            assertThat(user.id()).isEqualTo(createdUser.id());
                            assertThat(user.name()).isEqualTo(NAME_1);
                            assertThat(user.email()).isEqualTo(EMAIL);
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
            var request = new UserRequest(UPDATED_NAME, PASSWORD, EMAIL);

            // WHEN
            var updatedUser = userService.updateUser(id, request);

            // THEN
            assertThat(updatedUser).isNotNull()
                    .satisfies(user -> {
                            assertThat(user.id()).isEqualTo(createdUser.id());
                            assertThat(user.name()).isEqualTo(UPDATED_NAME);
                            assertThat(user.email()).isEqualTo(EMAIL);
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

    private static UserResponse createDefaultUser(UserService userService) {
        return userService.createUser(new UserRequest(NAME_1, PASSWORD, EMAIL));
    }

    private static List<UserResponse> createListOfUsers(UserService userService) {
        return List.of(
                userService.createUser(new UserRequest(NAME_1, PASSWORD, EMAIL)),
                userService.createUser(new UserRequest(NAME_2, PASSWORD, EMAIL))
        );
    }
}
