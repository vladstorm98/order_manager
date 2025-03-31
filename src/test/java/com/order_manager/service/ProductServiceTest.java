package com.order_manager.service;

import com.order_manager.client.ProductClient;
import com.order_manager.dto.ProductRequest;
import com.order_manager.dto.ProductResponse;
import com.order_manager.exception.ProductExistException;
import com.order_manager.exception.ProductNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit tests for ProductService")
public class ProductServiceTest {

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

    @Mock
    private ProductClient productClient;

    @InjectMocks
    private ProductService productService;

    @Test
    @DisplayName("""
            GIVEN a product request
            WHEN creating a new product
            THEN the created product should be returned with correct details
            """)
    void shouldCreateProduct() {
        //GIVEN
        var request = new ProductRequest(PRODUCT_NAME_NEW, PRODUCT_DESCRIPTION_NEW, PRODUCT_PRICE_NEW);
        var createdProduct = prepareProduct(PRODUCT_ID_NEW, PRODUCT_NAME_NEW, PRODUCT_PRICE_NEW);

        when(productClient.createProduct(request)).thenReturn(createdProduct);

        //WHEN
        var actualProduct = productService.createProduct(request);

        //THEN
        assertThat(actualProduct).isNotNull()
                .satisfies(product -> {
                    assertThat(product.id()).isEqualTo(createdProduct.id());
                    assertThat(product.name()).isEqualTo(createdProduct.name());
                    assertThat(product.price()).isEqualTo(createdProduct.price());
                });

        verify(productClient, times(1)).createProduct(request);
        verifyNoMoreInteractions(productClient);
    }

    @Test
    @DisplayName("""
            GIVEN a product request
            WHEN the product already exists
            THEN a ProductExistException should be thrown
            """)
    void shouldThrowException_whenProductExists() {
        //GIVEN
        var request = new ProductRequest(PRODUCT_NAME_NEW, PRODUCT_DESCRIPTION_NEW, PRODUCT_PRICE_NEW);

        //WHEN
        when(productClient.createProduct(request)).thenReturn(null);

        //THEN
        assertThatThrownBy(() -> productService.createProduct(request))
                .isInstanceOf(ProductExistException.class)
                .hasMessageContaining("Product  with name " + request.name() + " already exists");
    }

    @Test
    @DisplayName("""
            GIVEN the existing products
            WHEN fetching all products
            THEN all existing products should be returned with correct details
            """)
    void shouldGetAllProducts() {
        //GIVEN
        var expectedProducts = prepareProducts();

        when(productClient.getAllProducts()).thenReturn(expectedProducts);

        //WHEN
        var actualProducts = productService.getAllProducts();

        assertThat(actualProducts)
                .hasSize(expectedProducts.size())
                .containsExactlyInAnyOrderElementsOf(expectedProducts);

        assertThat(actualProducts.getFirst())
                .satisfies(product -> {
                    assertThat(product.id()).isEqualTo(expectedProducts.getFirst().id());
                    assertThat(product.name()).isEqualTo(expectedProducts.getFirst().name());
                    assertThat(product.price()).isEqualTo(expectedProducts.getFirst().price());
                });

        verify(productClient, times(1)).getAllProducts();
        verifyNoMoreInteractions(productClient);
    }

    @Test
    @DisplayName("""
            WHEN fetching empty list of products
            THEN a ProductNotFoundException should be thrown
            """)
    void shouldThrowException_whenAllProductsNotFound() {
        //WHEN
        when(productClient.getAllProducts()).thenReturn(Collections.emptyList());

        //THEN
        assertThatThrownBy(() -> productService.getAllProducts())
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageContaining("Products not found");
    }

    @Test
    @DisplayName("""
            GIVEN the existing product
            WHEN fetching a product
            THEN the existing product should be returned with correct details
            """)
    void shouldGetProduct() {
        //GIVEN
        var expectedProduct = prepareProduct();

        when(productClient.getProduct(expectedProduct.id())).thenReturn(expectedProduct);

        //WHEN
        ProductResponse actualProduct = productService.getProduct(expectedProduct.id());

        //THEN
        assertThat(actualProduct).isNotNull()
                .satisfies(product -> {
                    assertThat(product.id()).isEqualTo(expectedProduct.id());
                    assertThat(product.name()).isEqualTo(expectedProduct.name());
                    assertThat(product.price()).isEqualTo(expectedProduct.price());
                });

        verify(productClient, times(1)).getProduct(expectedProduct.id());
        verifyNoMoreInteractions(productClient);
    }

    @Test
    @DisplayName("""
            GIVEN the existing product
            WHEN update a product
            THEN the product should be updated and returned with correct details
            """)
    void shouldUpdateProduct() {
        //GIVEN
        var oldProduct = prepareProduct();
        var request = new ProductRequest(PRODUCT_NAME_NEW, PRODUCT_DESCRIPTION_NEW, PRODUCT_PRICE_NEW);
        var updatedProduct = prepareProduct(PRODUCT_ID_1, PRODUCT_NAME_NEW, PRODUCT_PRICE_NEW);

        when(productClient.updateProduct(oldProduct.id(), request)).thenReturn(updatedProduct);

        //WHEN
        ProductResponse actualProduct = productService.updateProduct(oldProduct.id(), request);

        //THEN
        assertThat(actualProduct).isNotNull()
                .satisfies(product -> {
                    assertThat(product.id()).isEqualTo(updatedProduct.id());
                    assertThat(product.name()).isEqualTo(updatedProduct.name());
                    assertThat(product.price()).isEqualTo(updatedProduct.price());
                });

        verify(productClient).updateProduct(oldProduct.id(), request);
        verifyNoMoreInteractions(productClient);
    }

    @Test
    @DisplayName("""
            GIVEN the existing product
            WHEN deleting a product
            THEN the product should be deleted
            """)
    void shouldDeleteProduct() {
        //GIVEN
        var product = prepareProduct();

        when(productClient.getProduct(product.id())).thenReturn(product);

        //WHEN
        productService.deleteProduct(product.id());

        //THEN
        verify(productClient, times(1)).deleteProduct(product.id());
        verifyNoMoreInteractions(productClient);
    }

    @Test
    @DisplayName("""
            GIVEN the existing product
            WHEN the product doesn't exist
            THEN a ProductNotFoundException should be thrown
            """)
    void shouldThrowException_whenProductNotFound() {
        //GIVEN
        var product = prepareProduct();

        //WHEN
        when(productClient.getProduct(product.id())).thenReturn(null);

        //THEN
        assertThatThrownBy(() -> productService.getProduct(product.id()))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageContaining("Product with id #" + product.id() + " not found");
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
