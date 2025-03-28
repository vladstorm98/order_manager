package com.order_manager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.order_manager.dto.ProductRequest;
import com.order_manager.dto.ProductResponse;
import com.order_manager.service.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("JUnit tests for ProductController")
class ProductControllerTest {

    private static final Long PRODUCT_ID_1 = 1L;
    private static final Long PRODUCT_ID_2 = 2L;
    private static final Long PRODUCT_ID_NEW = 10L;
    private static final String PRODUCT_NAME_1 = "Product_name_1";
    private static final String PRODUCT_NAME_2 = "Product_name_2";
    private static final String PRODUCT_NAME_NEW = "Product_name_new";
    private static final String PRODUCT_DESCRIPTION_NEW = "Product_description_new";
    private static final BigDecimal PRODUCT_PRICE_1 = BigDecimal.ONE;
    private static final BigDecimal PRODUCT_PRICE_2 = BigDecimal.TWO;
    private static final BigDecimal PRODUCT_PRICE_NEW = BigDecimal.TEN;

    @MockitoBean
    private ProductService productService;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;

    @Test
    @DisplayName("""
            GIVEN a product request
            WHEN creating a new product
            THEN the created product should be returned with correct details
            """)
    void shouldCreateProduct() throws Exception {
        //GIVEN
        var request = new ProductRequest(PRODUCT_NAME_NEW, PRODUCT_DESCRIPTION_NEW, PRODUCT_PRICE_NEW);
        var createdProduct = prepareProduct(PRODUCT_ID_NEW, PRODUCT_NAME_NEW, PRODUCT_PRICE_NEW);

        when(productService.createProduct(request)).thenReturn(createdProduct);

        //WHEN
        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))

                //THEN
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(PRODUCT_ID_NEW))
                .andExpect(jsonPath("$.name").value(PRODUCT_NAME_NEW))
                .andExpect(jsonPath("$.price").value(PRODUCT_PRICE_NEW));

        verify(productService, times(1)).createProduct(request);
        verifyNoMoreInteractions(productService);
    }

    @Test
    @DisplayName("""
            GIVEN the existing products
            WHEN fetching all products
            THEN all existing products should be returned with correct details
            """)
    void shouldGetAllProducts() throws Exception {
        //GIVEN
        var expectedProducts = prepareProducts();

        when(productService.getAllProducts()).thenReturn(expectedProducts);

        //WHEN
        mockMvc.perform(get("/products")
                        .contentType(MediaType.APPLICATION_JSON))

                //THEN
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$[0].id").value(PRODUCT_ID_1),
                        jsonPath("$[1].id").value(PRODUCT_ID_2),
                        jsonPath("$[0].name").value(PRODUCT_NAME_1),
                        jsonPath("$[1].name").value(PRODUCT_NAME_2),
                        jsonPath("$[0].price").value(PRODUCT_PRICE_1),
                        jsonPath("$[1].price").value(PRODUCT_PRICE_2)
                );

        verify(productService, times(1)).getAllProducts();
        verifyNoMoreInteractions(productService);
    }

    @Test
    @DisplayName("""
            GIVEN the existing product
            WHEN fetching a product
            THEN the product should be returned with correct details
            """)
    void shouldGetProduct() throws Exception {
        //GIVEN
        var expectedProduct = prepareProduct();

        when(productService.getProduct(expectedProduct.id())).thenReturn(expectedProduct);

        //WHEN
        mockMvc.perform(get("/products/" + expectedProduct.id())
                        .contentType(MediaType.APPLICATION_JSON))

                //WHEN
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id").value(PRODUCT_ID_1),
                        jsonPath("$.name").value(PRODUCT_NAME_1),
                        jsonPath("$.price").value(PRODUCT_PRICE_1)
                );

        verify(productService, times(1)).getProduct(expectedProduct.id());
        verifyNoMoreInteractions(productService);
    }

    @Test
    @DisplayName("""
            GIVEN the existing product
            WHEN update a product
            THEN the product should be updated and returned with correct details
            """)
    void shouldUpdateProduct() throws Exception {
        //GIVEN
        var oldProduct = prepareProduct();
        var request = new ProductRequest(PRODUCT_NAME_NEW, PRODUCT_DESCRIPTION_NEW, PRODUCT_PRICE_NEW);
        var updatedProduct = prepareProduct(PRODUCT_ID_NEW, PRODUCT_NAME_NEW, PRODUCT_PRICE_NEW);

        when(productService.updateProduct(oldProduct.id(), request)).thenReturn(updatedProduct);

        //WHEN
        mockMvc.perform(put("/products/" + oldProduct.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))

                //THEN
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id").value(PRODUCT_ID_NEW),
                        jsonPath("$.name").value(PRODUCT_NAME_NEW),
                        jsonPath("$.price").value(PRODUCT_PRICE_NEW)
                );

        verify(productService, times(1)).updateProduct(oldProduct.id(), request);
        verifyNoMoreInteractions(productService);
    }

    @Test
    @DisplayName("""
            GIVEN the existing product
            WHEN deleting a product
            THEN the product should be deleted
            """)
    void shouldDeleteProduct() throws Exception {
        //GIVEN
        var product = prepareProduct();

        //WHEN
        mockMvc.perform(delete("/products/" + product.id()))

                //THEN
                .andExpect(status().isOk());

        verify(productService, times(1)).deleteProduct(product.id());
        verifyNoMoreInteractions(productService);
    }

    private ProductResponse prepareProduct(Long id, String name, BigDecimal price) {
        return new ProductResponse(id, name, price);
    }

    private ProductResponse prepareProduct() {
        return prepareProduct(PRODUCT_ID_1, PRODUCT_NAME_1, PRODUCT_PRICE_1);
    }

    private List<ProductResponse> prepareProducts() {
        return List.of(
                prepareProduct(PRODUCT_ID_1, PRODUCT_NAME_1, PRODUCT_PRICE_1),
                prepareProduct(PRODUCT_ID_2, PRODUCT_NAME_2, PRODUCT_PRICE_2)
        );
    }
}
