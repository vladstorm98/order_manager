package com.order_manager.dto;

import java.util.List;

public record OrderRequest (List<Long> listOfProductId, int quantity) {
}
