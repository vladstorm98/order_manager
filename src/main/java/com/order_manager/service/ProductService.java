package com.order_manager.service;

import com.order_manager.dto.ProductRequest;
import com.order_manager.dto.ProductResponse;
import com.order_manager.entity.ProductEntity;
import com.order_manager.exception.ProductNotFoundException;
import com.order_manager.repository.ProductRepository;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public ProductResponse createProduct(ProductRequest request) {
        if (productRepository.findByName(request.name()).isPresent()) {
            throw new EntityExistsException("Product already exists");
        }

        ProductEntity product = ProductEntity.builder()
                .name(request.name())
                .description(request.description())
                .price(request.price())
                .build();

        return mapToResponse(productRepository.save(product));
    }

    public ProductResponse updateProduct(long id, ProductRequest request) {
        if (productRepository.findById(id).isEmpty()) {
            throw new ProductNotFoundException("Product not found");
        }

        ProductEntity product = productRepository.findById(id).get();
        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());

        return mapToResponse(productRepository.save(product));
    }

    public void deleteProduct(long id) {
        if (productRepository.findById(id).isEmpty()) {
            throw new ProductNotFoundException("Product not found");
        }

        productRepository.deleteById(id);
    }

    private ProductResponse mapToResponse(ProductEntity product) {
        return new ProductResponse(product);
    }
}

