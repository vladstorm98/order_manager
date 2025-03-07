package com.order_manager.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderRequest {
    private List<Long> listOfProductId;
    private int quantity;
}
