package com.order_manager.service;

import com.order_manager.dto.ProductRequest;
import com.order_manager.dto.ProductResponse;
import com.order_manager.entity.ProductEntity;
import com.order_manager.exception.ProductNotFoundException;
import com.order_manager.repository.ProductRepository;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<ProductResponse> getAllProducts() {
        List<ProductResponse> response =  productRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();

        log.info("Products were successfully retrieved");
        return response;
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

        ProductResponse response = toResponse(productRepository.save(product));

        log.info("Product with id #{} was created", response.id());
        return response;
    }

    public ProductResponse updateProduct(Long id, ProductRequest request) {
        if (productRepository.findById(id).isEmpty()) {
            throw new ProductNotFoundException("Product not found");
        }

        ProductEntity product = productRepository.findById(id).get();
        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());

        ProductResponse response = toResponse(productRepository.save(product));

        log.info("Product with id #{} was updated", id);
        return response;
    }

    public void deleteProduct(Long id) {
        if (productRepository.findById(id).isEmpty()) {
            throw new ProductNotFoundException("Product not found");
        }

        productRepository.deleteById(id);
        log.info("Product with id #{} was deleted", id);
    }

    private ProductResponse toResponse(ProductEntity product) {
        return new ProductResponse(product);
    }
}

