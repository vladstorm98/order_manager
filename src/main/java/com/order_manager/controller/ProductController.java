package com.order_manager.controller;

import com.order_manager.dto.ProductRequest;
import com.order_manager.dto.ProductResponse;
import com.order_manager.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Products")
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    @Operation(summary = "Get the list of all product")
    public List<ProductResponse> getProducts() {
        return productService.getAllProducts();
    }

    @PostMapping
    @Operation(summary = "Add a new product")
    public ProductResponse addProduct(@RequestBody ProductRequest request) {
        return productService.createProduct(request);
    }

    @PutMapping("/{productId}")
    @Operation(summary = "Update information about product by ID")
    public ProductResponse updateProduct(@PathVariable Long productId, @RequestBody ProductRequest request) {
        return productService.updateProduct(productId, request);
    }

    @DeleteMapping("/{productId}")
    @Operation(summary = "Delete the product by ID")
    public void deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
    }
}
