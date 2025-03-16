package com.order_manager.service;

import com.order_manager.client.ProductClient;
import com.order_manager.dto.ProductRequest;
import com.order_manager.dto.ProductResponse;
import com.order_manager.exception.ProductExistException;
import com.order_manager.exception.ProductNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductClient productClient;

    @InjectMocks
    private ProductService productService;

    @Test
    void shouldReturnAllProducts() {
        List<ProductResponse> products = List.of(
                new ProductResponse(1L, "Product 1", 100),
                new ProductResponse(2L, "Product 2", 200)
        );
        when(productClient.getAllProducts()).thenReturn(products);

        List<ProductResponse> result = productService.getAllProducts();

        assertEquals(products.size(), result.size());
        assertEquals(products.getFirst(), result.getFirst());
        verify(productClient).getAllProducts();
    }

    @Test
    void shouldThrowExceptionWhenAllProductsNotFound() {
        when(productClient.getAllProducts()).thenReturn(Collections.emptyList());

        assertThrows(ProductNotFoundException.class, productService::getAllProducts);
    }

    @Test
    void shouldReturnProduct() {
        long id = 1L;
        ProductResponse product = new ProductResponse(id, "Product", 100);
        when(productClient.getProduct(id)).thenReturn(product);

        ProductResponse result = productService.getProduct(id);

        assertEquals(product, result);
        verify(productClient).getProduct(id);
    }

    @Test
    void shouldThrowExceptionWhenProductNotFound() {
        when(productClient.getProduct(anyLong())).thenReturn(null);

        assertThrows(ProductNotFoundException.class, () -> productService.getProduct(anyLong()));
    }

    @Test
    void shouldCreateProduct() {
        ProductRequest request = new ProductRequest("Product", "Description", 100);
        ProductResponse product = new ProductResponse(1L, "Product", 100);
        when(productClient.createProduct(request)).thenReturn(product);

        ProductResponse result = productService.createProduct(request);

        assertEquals(product, result);
        verify(productClient).createProduct(request);
    }

    @Test
    void shouldThrowExceptionWhenProductAlreadyExist() {
        when(productClient.createProduct(any())).thenReturn(null);

        assertThrows(ProductExistException.class, () -> productService.createProduct(any()));
    }

    @Test
    void shouldUpdateProduct() {
        long id = 1L;
        ProductRequest request = new ProductRequest("Product", "Description", 250);
        ProductResponse product = new ProductResponse(id, "Product", 250);
        when(productClient.updateProduct(id, request)).thenReturn(product);

        ProductResponse result = productService.updateProduct(id, request);

        assertEquals(product, result);
        verify(productClient).updateProduct(id, request);
    }

    @Test
    void shouldThrowExceptionWhenProductNotFoundForUpdate() {
        when(productClient.updateProduct(anyLong(), any())).thenReturn(null);

        assertThrows(ProductNotFoundException.class, () -> productService.updateProduct(anyLong(), any()));
    }

    @Test
    void shouldDeleteProduct() {
        long id = 1L;
        ProductResponse product = new ProductResponse(id, "Product", 100);
        when(productClient.getProduct(id)).thenReturn(product);

        productService.deleteProduct(id);

        verify(productClient).deleteProduct(id);
    }

    @Test
    void shouldThrowExceptionWhenProductNotFoundForDelete() {
        when(productClient.getProduct(anyLong())).thenReturn(null);

        assertThrows(ProductNotFoundException.class, () -> productService.deleteProduct(anyLong()));
    }
}
