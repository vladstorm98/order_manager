package com.order_manager.service;

import com.order_manager.client.ProductClient;
import com.order_manager.dto.ProductRequest;
import com.order_manager.dto.ProductResponse;
import com.order_manager.exception.ProductExistException;
import com.order_manager.exception.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductClient productClient;

    public List<ProductResponse> getAllProducts() {
        List<ProductResponse> response = Optional.ofNullable(productClient.getAllProducts())
                    .filter(products -> !products.isEmpty())
                    .orElseThrow(() -> new ProductNotFoundException("Products not found"));

        log.info("Products were retrieved");
        return response;
    }

    public ProductResponse createProduct(ProductRequest request) {
        ProductResponse response = Optional.ofNullable(productClient.createProduct(request))
                .orElseThrow(() -> new ProductExistException("Product already exists"));

        log.info("Product with id #{} was created", response.id());
        return response;
    }

    public ProductResponse getProduct(Long id) {
        ProductResponse response = Optional.ofNullable(productClient.getProduct(id))
                    .orElseThrow(() -> new ProductNotFoundException("Product with ID " + id + " not found"));

        log.info("Product with id #{} was retrieved", id);
        return response;
    }

    public ProductResponse updateProduct(long id, ProductRequest request) {
        ProductResponse response = Optional.ofNullable(productClient.updateProduct(id, request))
                    .orElseThrow(() -> new ProductNotFoundException("Product with ID " + id + " not found"));

        log.info("Product with id #{} was updated", id);
        return response;
    }

    public void deleteProduct(long id) {
        Optional.ofNullable(productClient.getProduct(id))
                .ifPresentOrElse(_ -> productClient.deleteProduct(id), () -> {
                    throw new ProductNotFoundException("Product with ID " + id + " not found for delete");
                });
        log.info("Product with id #{} was deleted", id);
    }
}
