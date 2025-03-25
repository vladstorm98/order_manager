package com.order_manager.controller;

import com.order_manager.dto.AuthResponse;
import com.order_manager.dto.UserRequest;
import com.order_manager.security.JwtTokenProvider;
import com.order_manager.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Authentication")
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/register")
    @Operation(summary = "Registration a new user")
    public void register(@RequestBody @Valid UserRequest request) {
        userService.createUser(request);
    }

    @PostMapping("/login")
    @Operation(summary = "User authorization")
    public AuthResponse login(@RequestBody UserRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.name(), request.password()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenProvider.createToken(request.name());

        return new AuthResponse(token);
    }

    @PostMapping("/logout")
    @Operation(summary = "User logout")
    public void logout() {
        SecurityContextHolder.clearContext();
    }
}

