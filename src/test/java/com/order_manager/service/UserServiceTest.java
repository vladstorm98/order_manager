package com.order_manager.service;

import com.order_manager.dto.UserDTO;
import com.order_manager.dto.UserInput;
import com.order_manager.entity.UserEntity;
import com.order_manager.entity.UserRole;
import com.order_manager.mapper.UserMapper;
import com.order_manager.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.doNothing;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit tests for UserService")
public class UserServiceTest {

    private static final Long USER_ID_1 = 1L;
    private static final Long USER_ID_2 = 2L;
    private static final Long USER_ID_NEW = 10L;
    private static final String USER_NAME_1 = "Name_1";
    private static final String USER_NAME_2 = "Name_2";
    private static final String USER_NAME_NEW = "NewName";
    private static final String USER_PASSWORD = "Password";
    private static final UserRole USER_ROLE = UserRole.USER;
    private static final String USER_EMAIL_1 = "email_1@example.com";
    private static final String USER_EMAIL_2 = "email_2@example.com";
    private static final String USER_EMAIL_NEW = "email_new@example.com";

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserMapper userMapper;
    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("""
            GIVEN User input
            WHEN Creating a new user
            THEN Created user should be returned with correct data
            """)
    void shouldCreateUser() {
        // GIVEN
        var input = new UserInput(USER_NAME_NEW, USER_PASSWORD, USER_EMAIL_NEW);
        var userEntity = buildEntity(USER_ID_NEW, USER_NAME_NEW, USER_EMAIL_NEW);
        var createdUser = buildResponse(USER_ID_NEW, USER_NAME_NEW, USER_EMAIL_NEW);

        when(userRepository.findByName(input.name())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(input.password())).thenReturn(USER_PASSWORD);
        when(userMapper.dbToDto(any())).thenReturn(createdUser);
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        // WHEN
        var actualUser = userService.createUser(input);

        // THEN
        assertThat(actualUser).isNotNull()
                .satisfies(user -> {
                    assertThat(user.name()).isEqualTo(input.name());
                    assertThat(user.email()).isEqualTo(input.email());
                });

        verify(userRepository, times(1)).findByName(input.name());
        verify(passwordEncoder, times(1)).encode(input.password());
        verify(userMapper, times(1)).dbToDto(any());
        verify(userRepository, times(1)).save(any(UserEntity.class));
        verifyNoMoreInteractions(userRepository, passwordEncoder, userMapper);
    }



    @Test
    @DisplayName("""
        GIVEN List of existing users
        WHEN Fetching all users
        THEN All existing users should be returned with correct data
        """)
    void shouldGetAllUsers() {
        // GIVEN
        var expectedUsers = buildResponses();
        var userEntities = buildEntities();

        when(userRepository.findAll()).thenReturn(userEntities);
        when(userMapper.dbToDto(userEntities.get(0))).thenReturn(expectedUsers.get(0));
        when(userMapper.dbToDto(userEntities.get(1))).thenReturn(expectedUsers.get(1));

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

        verify(userRepository, times(1)).findAll();
        verify(userMapper, times(expectedUsers.size())).dbToDto(any());
        verifyNoMoreInteractions(userRepository, userMapper);
    }

    @Test
    @DisplayName("""
        GIVEN Existing user
        WHEN Fetching the user by id
        THEN User should be returned
        """)
    void shouldGetUserById() {
        // GIVEN
        var expectedUser = buildResponse();
        var userEntity = buildEntity();

        when(userRepository.findById(expectedUser.id())).thenReturn(Optional.of(userEntity));
        when(userMapper.dbToDto(userEntity)).thenReturn(expectedUser);

        // WHEN
        var actualUser = userService.getUserById(expectedUser.id());

        // THEN
        assertThat(actualUser).isNotNull()
                .satisfies( user -> {
                    assertThat(user.id()).isEqualTo(expectedUser.id());
                    assertThat(user.name()).isEqualTo(expectedUser.name());
                    assertThat(user.email()).isEqualTo(expectedUser.email());
                });

        verify(userRepository, times(1)).findById(expectedUser.id());
        verify(userMapper, times(1)).dbToDto(any());
        verifyNoMoreInteractions(userRepository, userMapper);
    }

    @Test
    @DisplayName("""
        GIVEN Existing user and a user input
        WHEN Updating the user
        THEN Updated user should be updated with new values
        """)
    void shouldUpdateUser() {
        // GIVEN
        var oldUser = buildEntity();
        var input = new UserInput(USER_NAME_NEW, USER_PASSWORD, USER_EMAIL_NEW);
        var userEntity = buildEntity(USER_ID_1, USER_NAME_NEW, USER_EMAIL_NEW);
        var updatedUser = buildResponse(USER_ID_1, USER_NAME_NEW, USER_EMAIL_NEW);

        when(userRepository.findById(oldUser.getId())).thenReturn(Optional.of(oldUser));
        when(passwordEncoder.encode(input.password())).thenReturn(USER_PASSWORD);
        when(userMapper.dbToDto(any())).thenReturn(updatedUser);
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        // WHEN
        var actualUser = userService.updateUser(oldUser.getId(), input);

        // THEN
        assertThat(actualUser).isNotNull()
                .satisfies(user -> {
                    assertThat(user.id()).isEqualTo(oldUser.getId());
                    assertThat(user.name()).isEqualTo(input.name());
                    assertThat(user.email()).isEqualTo(input.email());
                });

        verify(userRepository, times(1)).findById(oldUser.getId());
        verify(passwordEncoder, times(1)).encode(input.password());
        verify(userMapper, times(1)).dbToDto(any());
        verify(userRepository, times(1)).save(any(UserEntity.class));
        verifyNoMoreInteractions(userRepository, passwordEncoder, userMapper);
    }

    @Test
    @DisplayName("""
        Should delete the existing user by id:
            GIVEN Existing user
            WHEN Deleting the user
            THEN User should be deleted
        """)
    void shouldDeleteUser() {
        // GIVEN
        var user = buildEntity();

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        doNothing().when(userRepository).delete(user);
        // WHEN
        userService.deleteUser(user.getId());

        // THEN
        verify(userRepository, times(1)).findById(user.getId());
        verify(userRepository, times(1)).delete(user);
        verifyNoMoreInteractions(userRepository);
    }

    private UserEntity buildEntity(Long id, String name, String email) {
        return new UserEntity(id, name, USER_PASSWORD, USER_ROLE, email);
    }

    private UserEntity buildEntity() {
        return buildEntity(USER_ID_1, USER_NAME_1, USER_EMAIL_1);
    }

    private List<UserEntity> buildEntities() {
        return List.of(
                buildEntity(USER_ID_1, USER_NAME_1, USER_EMAIL_1),
                buildEntity(USER_ID_2, USER_NAME_2, USER_EMAIL_2)
        );
    }

    private UserDTO buildResponse(Long id, String name, String email) {
        return new UserDTO(id, name, email);
    }

    private UserDTO buildResponse() {
        return buildResponse(USER_ID_1, USER_NAME_1, USER_EMAIL_1);
    }

    private List<UserDTO> buildResponses() {
        return List.of(
                buildResponse(USER_ID_1, USER_NAME_1, USER_EMAIL_1),
                buildResponse(USER_ID_2, USER_NAME_2, USER_EMAIL_2)
        );
    }
}
