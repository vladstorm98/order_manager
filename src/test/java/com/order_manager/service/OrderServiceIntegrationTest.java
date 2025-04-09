package com.order_manager.service;

import com.order_manager.BaseTest;
import com.order_manager.dto.OrderDTO;
import com.order_manager.dto.OrderInput;
import com.order_manager.entity.DbProduct;
import com.order_manager.entity.OrderStatus;
import com.order_manager.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Integration tests for OrderService")
public class OrderServiceIntegrationTest extends BaseTest {

    private static final String USER_NAME = "Name_1";

    private static final Long PRODUCT_ID_1 = 1L;
    private static final Long PRODUCT_ID_2 = 2L;
    private static final String PRODUCT_NAME_1 = "Product 1";
    private static final String PRODUCT_NAME_2 = "Product 2";
    private static final String PRODUCT_DESCRIPTION = "Product Description";
    private static final BigDecimal PRODUCT_PRICE_1 = BigDecimal.ONE;
    private static final BigDecimal PRODUCT_PRICE_2 = BigDecimal.TWO;

    private static final Long ORDER_ID_1 = 1L;
    private static final Long ORDER_ID_2 = 2L;
    private static final Integer ORDER_QUANTITY = 1;
    private static final OrderStatus ORDER_STATUS = OrderStatus.PENDING;
    private static final OrderStatus ORDER_STATUS_NEW = OrderStatus.COMPLETED;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderService orderService;

    @Test
    @DisplayName("""
            GIVEN Input for creating a new order
            WHEN Creating a new order
            THEN Created order should be returned and saved in database
            """)
    public void shouldCreateOrder() {
        //GIVEN
        var products = prepareProducts();
        var input = new OrderInput(products, ORDER_QUANTITY);

        //WHEN
        var createdOrder = orderService.createOrder(USER_NAME, input);

        //THEN
        assertThat(createdOrder).isNotNull()
                .satisfies(order -> {
                    assertThat(order.status()).isEqualTo(ORDER_STATUS);
                    assertThat(order.products()).hasSize(products.size());
                    assertThat(order.products()).containsExactlyInAnyOrderElementsOf(products);
                });

        assertThat(orderRepository.findById(createdOrder.id())).isPresent();
    }

    @Test
    @DisplayName("""
            GIVEN List of all user orders in the database
            WHEN Fetching all user orders from the repository
            THEN All user orders from the database should be returned
            """)
    public void shouldGetAllOrdersForUser() {
        //GIVEN
        var expectedOrders = buildDTOs();

        //WHEN
        var actualOrders = orderService.getAllOrdersForUser(USER_NAME);

        //THEN
        assertThat(actualOrders).isNotNull()
                .hasSize(expectedOrders.size())
                .containsExactlyInAnyOrderElementsOf(expectedOrders);

        assertThat(actualOrders.getFirst()).isNotNull()
                .satisfies(order -> {
                    assertThat(order.id()).isEqualTo(expectedOrders.getFirst().id());
                    assertThat(order.status()).isEqualTo(expectedOrders.getFirst().status());
                    assertThat(order.products()).hasSize(expectedOrders.getFirst().products().size());
                    assertThat(order.products()).containsExactlyInAnyOrderElementsOf(expectedOrders.getFirst().products());
                });
    }

    @Test
    @DisplayName("""
            GIVEN Existed order and a new status for updating
            WHEN Updating the status of the order
            THEN Updated status should be updated with new values and saved in the database
            """)
    void shouldUpdateOrderStatus() {
        // GIVEN
        var expectedOrder = buildDTO(ORDER_ID_1, ORDER_STATUS_NEW, prepareProducts());

        // WHEN
        var updatedUser = orderService.updateOrderStatus(expectedOrder.id(), ORDER_STATUS_NEW);

        // THEN
        assertThat(updatedUser).isNotNull()
                .satisfies(order -> {
                    assertThat(order.id()).isEqualTo(expectedOrder.id());
                    assertThat(order.status()).isEqualTo(expectedOrder.status());
                    assertThat(order.products()).hasSize(expectedOrder.products().size());
                    assertThat(order.products()).containsExactlyInAnyOrderElementsOf(expectedOrder.products());
                });

        assertThat(orderRepository.findById(expectedOrder.id())).isPresent();
    }

    @Test
    @DisplayName("""
            GIVEN Existed order
            WHEN Deleting the order
            THEN Order should be deleted from the database
            """)
    void shouldDeleteOrder() {
        // GIVEN
        var order = buildDTO();

        // WHEN
        orderService.deleteOrder(order.id());

        // THEN
        assertThat(orderRepository.existsById(order.id())).isFalse();
    }

    private List<DbProduct> prepareProducts() {
        return List.of(
                new DbProduct(PRODUCT_ID_1, PRODUCT_NAME_1, PRODUCT_DESCRIPTION, PRODUCT_PRICE_1),
                new DbProduct(PRODUCT_ID_2, PRODUCT_NAME_2, PRODUCT_DESCRIPTION, PRODUCT_PRICE_2)
        );
    }

    private OrderDTO buildDTO(Long id, OrderStatus status, List<DbProduct> products) {
        return new OrderDTO(id, status, products);
    }

    private OrderDTO buildDTO() {
        return buildDTO(ORDER_ID_1, ORDER_STATUS, prepareProducts());
    }

    private List<OrderDTO> buildDTOs() {
        return List.of(
                buildDTO(ORDER_ID_1, ORDER_STATUS, prepareProducts()),
                buildDTO(ORDER_ID_2, ORDER_STATUS, prepareProducts())
        );
    }
}
