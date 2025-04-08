package com.order_manager.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ProductInput(
        @NotBlank
        @Size(min = 4, max = 40)
        String name,

        @NotBlank
        @NotEmpty
        @Size(min = 40, max = 400)
        String description,

        @Min(0)
        BigDecimal price
) {}
