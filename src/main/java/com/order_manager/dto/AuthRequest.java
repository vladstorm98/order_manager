package com.order_manager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthRequest {

    @Schema(example = "login")
    @NotBlank
    @Size(min = 3, max = 16)
    private String username;

    @Schema(example = "password")
    @NotBlank
    @Size(min = 4, max = 16)
    private String password;
}
