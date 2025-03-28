package com.order_manager.controller;

import com.order_manager.dto.UserRequest;
import com.order_manager.dto.UserResponse;
import com.order_manager.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Users")
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}")
    @Operation(summary ="Get user by ID")
    public UserResponse getUser(@PathVariable Long userId) {
        return userService.getUserById(userId);
    }

    @PostMapping
    @Operation(summary = "Create a new user")
    public UserResponse createUser(@RequestBody UserRequest request) {
        return userService.createUser(request);
    }

    @PutMapping("/{userId}")
    @Operation(summary = "Update user by ID")
    public UserResponse updateUser(@PathVariable Long userId, @RequestBody UserRequest request) {
        return userService.updateUser(userId, request);
    }

    @DeleteMapping("/{userId}")
    @Operation(summary = "Delete user by ID")
    public void deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
    }
}
