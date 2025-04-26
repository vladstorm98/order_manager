package com.order_manager.client;

import com.order_manager.dto.ProductInput;
import com.order_manager.dto.ProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "product-service", url = "http://localhost:8080/")
public interface ProductClient {

    @GetMapping("/products")
    List<ProductDto> getAllProducts();

    @PostMapping("/products")
    ProductDto createProduct(@RequestBody ProductInput request);

    @GetMapping("products/{id}")
    ProductDto getProduct(@PathVariable Long id);

    @PutMapping("/products/{id}")
    ProductDto updateProduct(@PathVariable Long id, @RequestBody ProductInput request);

    @DeleteMapping("/products/{id}")
    void deleteProduct(@PathVariable Long id);
}
