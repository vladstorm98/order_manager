package com.order_manager.service;

import com.order_manager.dto.ProductRequest;
import com.order_manager.dto.ProductResponse;
import com.order_manager.entity.ProductEntity;
import com.order_manager.exception.ProductNotFoundException;
import com.order_manager.repository.ProductRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public ProductResponse createProduct(ProductRequest request) {
        if (productRepository.findByName(request.getName()).isPresent()) {
            throw new EntityExistsException("Product already exists");
        }

        ProductEntity product = ProductEntity.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .build();

        return mapToResponse(productRepository.save(product));
    }

    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public ProductResponse updateProduct(long id, ProductRequest request) {
        if (productRepository.findById(id).isEmpty()) {
            throw new ProductNotFoundException("Product not found");
        }

        ProductEntity product = productRepository.findById(id).get();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());

        return mapToResponse(productRepository.save(product));
    }

    public void deleteProduct(long id) {
        if (productRepository.findById(id).isEmpty()) {
            throw new EntityNotFoundException("Product not found");
        }

        productRepository.deleteById(id);
    }

    private ProductResponse mapToResponse(ProductEntity product) {
        return new ProductResponse(product);
    }
}

