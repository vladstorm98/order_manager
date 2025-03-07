package com.order_manager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthRequest {

    @NotBlank
    @Size(min = 3, max = 16)
    private String username;

    @NotBlank
    @Size(min = 4, max = 16)
    private String password;
}
