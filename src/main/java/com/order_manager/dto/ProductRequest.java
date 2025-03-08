package com.order_manager.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequest {

    @NotBlank
    @Size(min = 4, max = 40)
    private String name;

    @NotBlank
    @NotEmpty
    @Size(min = 40, max = 400)
    private String description;

    @NotBlank
    @Min(0)
    private double price;
}
