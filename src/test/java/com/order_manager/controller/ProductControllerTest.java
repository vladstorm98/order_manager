package com.order_manager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.order_manager.Application;
import com.order_manager.dto.ProductRequest;
import com.order_manager.dto.ProductResponse;
import com.order_manager.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
class ProductControllerTest {

    @MockitoBean
    private ProductService productService;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;

    @Test
    void shouldReturnAllProducts() throws Exception {
        List<ProductResponse> products = List.of(
                new ProductResponse(1L, "Product 1", 100),
                new ProductResponse(2L, "Product 2", 200)
        );
        when(productService.getAllProducts()).thenReturn(products);

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[1].name").value("Product 2"));

        verify(productService, times(1)).getAllProducts();
    }

    @Test
    void shouldReturnProductById() throws Exception {
        long id = 1L;
        ProductResponse product = new ProductResponse(id, "Product", 100);
        when(productService.getProduct(id)).thenReturn(product);

        mockMvc.perform(get("/products/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Product"));

        verify(productService, times(1)).getProduct(id);
    }

    @Test
    void shouldCreateProduct() throws Exception {
        ProductRequest request = new ProductRequest("Product", "Description", 100);
        ProductResponse product = new ProductResponse(1L, "Product", 100);
        String json = mapper.writeValueAsString(request);
        when(productService.createProduct(request)).thenReturn(product);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        verify(productService, times(1)).createProduct(request);
    }

    @Test
    void shouldUpdateProduct() throws Exception {
        long id = 1L;
        ProductRequest request = new ProductRequest("Product", "Description", 100);
        ProductResponse product = new ProductResponse(1L, "Product", 100);
        String json = mapper.writeValueAsString(request);
        when(productService.updateProduct(id, request)).thenReturn(product);

        mockMvc.perform(put("/products/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        verify(productService, times(1)).updateProduct(id, request);
    }

    @Test
    void shouldDeleteProduct() throws Exception {
        long id = 1L;
        doNothing().when(productService).deleteProduct(id);

        mockMvc.perform(delete("/products/" + id))
                .andExpect(status().isOk());

        verify(productService, times(1)).deleteProduct(id);
    }
}
