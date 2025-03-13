package com.order_manager.service;

import com.order_manager.client.ProductClient;
import com.order_manager.dto.ProductRequest;
import com.order_manager.dto.ProductResponse;
import com.order_manager.exception.ProductCreationException;
import com.order_manager.exception.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductClient productClient;

    public List<ProductResponse> getAllProducts() {
        return Optional.ofNullable(productClient.getAllProducts())
                    .filter(products -> !products.isEmpty())
                    .orElseThrow(() -> new ProductNotFoundException("Products not found"));
    }

    public ProductResponse getProduct(Long id) {
        return Optional.ofNullable(productClient.getProduct(id))
                    .orElseThrow(() -> new ProductNotFoundException("Product with ID " + id + " not found"));
    }

    public ProductResponse createProduct(ProductRequest request) {
        return Optional.ofNullable(productClient.createProduct(request))
                    .orElseThrow(() -> new ProductCreationException("Product creation failed"));
    }

    public ProductResponse updateProduct(long id, ProductRequest request) {
        return Optional.ofNullable(productClient.updateProduct(id, request))
                    .orElseThrow(() -> new ProductNotFoundException("Product with ID " + id + " not found"));
    }

    public void deleteProduct(long id) {
        productClient.deleteProduct(id);
    }
}

