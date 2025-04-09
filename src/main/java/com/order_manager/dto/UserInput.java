package com.order_manager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserInput(
        @Schema(example = "vlad")
        @NotBlank
        @Size(min = 3, max = 16)
        String name,

        @Schema(example = "1111")
        @NotBlank
        @Size(min = 4, max = 16)
        String password,

        @Schema(example = "vlad@gmail.com")
        @NotBlank
        @Size(min = 10, max = 40)
        String email
) {}
