package com.order_manager.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record OrderRequest (

        @Schema(example = "[1, 2]")
        List<Long> listOfProductId,

        @Schema(example = "1")
        int quantity) {
}
