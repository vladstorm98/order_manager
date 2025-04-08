package com.order_manager.client;

import com.order_manager.dto.ProductInput;
import com.order_manager.dto.ProductDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "product-service", url = "http://localhost:8080/")
public interface ProductClient {

    @GetMapping("/products")
    List<ProductDTO> getAllProducts();

    @PostMapping("/products")
    ProductDTO createProduct(@RequestBody ProductInput request);

    @GetMapping("products/{id}")
    ProductDTO getProduct(@PathVariable Long id);

    @PutMapping("/products/{id}")
    ProductDTO updateProduct(@PathVariable Long id, @RequestBody ProductInput request);

    @DeleteMapping("/products/{id}")
    void deleteProduct(@PathVariable Long id);
}
