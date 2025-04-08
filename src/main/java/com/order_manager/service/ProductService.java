package com.order_manager.service;

import com.order_manager.client.ProductClient;
import com.order_manager.dto.ProductInput;
import com.order_manager.dto.ProductDTO;
import com.order_manager.exception.ProductExistException;
import com.order_manager.exception.ProductNotFoundException;
import lombok.NonNull;
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

    public List<ProductDTO> getAllProducts() {
        List<ProductDTO> response = Optional.ofNullable(productClient.getAllProducts())
                    .filter(products -> !products.isEmpty())
                    .orElseThrow(() -> new ProductNotFoundException("Products not found"));

        log.info("Products were retrieved");
        return response;
    }

    public ProductDTO createProduct(ProductInput input) {
        ProductDTO response = Optional.ofNullable(productClient.createProduct(input))
                .orElseThrow(() -> new ProductExistException("Product  with name " + input.name() + " already exists"));

        log.info("Product with id #{} was created", response.id());
        return response;
    }

    public ProductDTO getProduct(@NonNull Long id) {
        ProductDTO response = Optional.ofNullable(productClient.getProduct(id))
                    .orElseThrow(() -> new ProductNotFoundException("Product with id #" + id + " not found"));

        log.info("Product with id #{} was retrieved", id);
        return response;
    }

    public ProductDTO updateProduct(@NonNull Long id, ProductInput input) {
        ProductDTO response = Optional.ofNullable(productClient.updateProduct(id, input))
                    .orElseThrow(() -> new ProductNotFoundException("Product with id #" + id + " not found"));

        log.info("Product with id #{} was updated", id);
        return response;
    }

    public void deleteProduct(@NonNull Long id) {
        Optional.ofNullable(productClient.getProduct(id))
                .ifPresentOrElse(_ -> productClient.deleteProduct(id), () -> {
                    throw new ProductNotFoundException("Product with id #" + id + " not found for delete");
                });
        log.info("Product with id #{} was deleted", id);
    }
}
