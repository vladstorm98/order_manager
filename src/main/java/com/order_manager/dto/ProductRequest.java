package com.order_manager.dto;

import jakarta.validation.constraints.*;

public record ProductRequest(
        @NotBlank
        @Size(min = 4, max = 40)
        String name,

        @NotBlank
        @NotEmpty
        @Size(min = 40, max = 400)
        String description,

        @Min(0)
        double price
) {}
