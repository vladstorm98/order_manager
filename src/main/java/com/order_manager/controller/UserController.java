package com.order_manager.controller;

import com.order_manager.dto.UserDto;
import com.order_manager.dto.UserInput;
import com.order_manager.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Users")
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    @Operation(summary = "Create a new user")
    public UserDto createUser(@RequestBody UserInput input) {
        return userService.createUser(input);
    }

    @GetMapping()
    @Operation(summary ="Get all users")
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    @Operation(summary ="Get user by ID")
    public UserDto getUser(@PathVariable Long userId) {
        return userService.getUserById(userId);
    }

    @PutMapping("/{userId}")
    @Operation(summary = "Update user by ID")
    public UserDto updateUser(@PathVariable Long userId, @RequestBody UserInput input) {
        return userService.updateUser(userId, input);
    }

    @DeleteMapping("/{userId}")
    @Operation(summary = "Delete user by ID")
    public void deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
    }
}
