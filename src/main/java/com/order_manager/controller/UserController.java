package com.order_manager.controller;

import com.order_manager.dto.UserDTO;
import com.order_manager.dto.UserInput;
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
    public UserDTO getUser(@PathVariable Long userId) {
        return userService.getUserById(userId);
    }

    @PostMapping
    @Operation(summary = "Create a new user")
    public UserDTO createUser(@RequestBody UserInput input) {
        return userService.createUser(input);
    }

    @PutMapping("/{userId}")
    @Operation(summary = "Update user by ID")
    public UserDTO updateUser(@PathVariable Long userId, @RequestBody UserInput input) {
        return userService.updateUser(userId, input);
    }

    @DeleteMapping("/{userId}")
    @Operation(summary = "Delete user by ID")
    public void deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
    }
}
