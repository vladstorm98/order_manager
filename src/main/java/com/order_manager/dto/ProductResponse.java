package com.order_manager.dto;

import com.order_manager.entity.ProductEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse implements ResponseDTO {
    private long id;
    private String name;
    private double price;

    public ProductResponse(ProductEntity product) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
    }

}
