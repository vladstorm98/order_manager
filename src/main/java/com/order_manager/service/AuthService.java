package com.order_manager.service;

import com.order_manager.entity.UserRole;
import com.order_manager.entity.UserEntity;
import com.order_manager.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void registerUser(String username, String password, UserRole role) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new EntityExistsException("User with username " + username + " already exists");
        }

        UserEntity user = new UserEntity(username, passwordEncoder.encode(password), role);
        userRepository.save(user);
    }
}
