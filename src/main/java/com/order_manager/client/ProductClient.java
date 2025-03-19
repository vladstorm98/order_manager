package com.order_manager.client;

import com.order_manager.dto.ProductRequest;
import com.order_manager.dto.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "product-service", url = "http://localhost:8080/")
public interface ProductClient {

    @GetMapping("/products")
    List<ProductResponse> getAllProducts();

    @GetMapping("products/{id}")
    ProductResponse getProduct(@PathVariable Long id);

    @PostMapping("/products")
    ProductResponse createProduct(@RequestBody ProductRequest request);

    @PutMapping("/products/{id}")
    ProductResponse updateProduct(@PathVariable Long id, @RequestBody ProductRequest request);

    @DeleteMapping("/products/{id}")
    void deleteProduct(@PathVariable Long id);
}
