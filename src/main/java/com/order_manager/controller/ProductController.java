package com.order_manager.controller;

import com.order_manager.dto.ProductInput;
import com.order_manager.dto.ProductDto;
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
    public List<ProductDto> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get information about product by ID")
    public ProductDto getProduct(@PathVariable Long id) {
        return productService.getProduct(id);
    }

    @PostMapping
    @Operation(summary = "Create a new product")
    public ProductDto createProduct(@RequestBody ProductInput input) {
        return productService.createProduct(input);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update information about product by ID")
    public ProductDto updateProduct(@PathVariable Long id, @RequestBody ProductInput input) {
        return productService.updateProduct(id, input);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete the product by ID")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
}
