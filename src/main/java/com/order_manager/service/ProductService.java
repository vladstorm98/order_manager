package com.order_manager.service;

import com.order_manager.entity.ProductEntity;
import com.order_manager.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public ProductEntity createProduct(String name, String description, double price) {
        if (productRepository.findByName(name).isPresent()) {
            throw new IllegalArgumentException("Product already exists");
        }

        ProductEntity product = ProductEntity.builder()
                .name(name)
                .description(description)
                .price(price)
                .build();

        return productRepository.save(product);
    }

    public List<ProductEntity> getAllProducts() {
        return productRepository.findAll();
    }
}

