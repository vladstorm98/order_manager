package com.order_manager.service;

import com.order_manager.dto.OrderDto;
import com.order_manager.dto.OrderInput;
import com.order_manager.entity.*;
import com.order_manager.exception.OrderNotFoundException;
import com.order_manager.exception.ProductNotFoundException;
import com.order_manager.exception.UserNotFoundException;
import com.order_manager.mapper.OrderMapper;
import com.order_manager.repository.OrderRepository;
import com.order_manager.repository.ProductRepository;
import com.order_manager.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit tests for OrderService")
public class OrderServiceTest {

    private static final String USER_NAME = "Name";
    private static final String USER_PASSWORD = "Password";
    private static final UserRole USER_ROLE = UserRole.USER;
    private static final String USER_EMAIL = "email@example.com";

    private static final Long PRODUCT_ID_1 = 1L;
    private static final Long PRODUCT_ID_2 = 2L;
    private static final String PRODUCT_NAME_1 = "Product 1";
    private static final String PRODUCT_NAME_2 = "Product 2";
    private static final String PRODUCT_DESCRIPTION = "Product Description";
    private static final BigDecimal PRODUCT_PRICE_1 = BigDecimal.ONE;
    private static final BigDecimal PRODUCT_PRICE_2 = BigDecimal.TWO;

    private static final Long ORDER_ID_1 = 1L;
    private static final Long ORDER_ID_2 = 2L;
    private static final Long ORDER_ID_NEW = 10L;
    private static final Integer ORDER_QUANTITY = 1;
    private static final OrderStatus ORDER_STATUS = OrderStatus.PENDING;
    private static final OrderStatus ORDER_STATUS_NEW = OrderStatus.COMPLETED;

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private NotificationService notificationService;
    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderService orderService;

    @Test
    @DisplayName("""
            GIVEN Input for creating a new order
            WHEN Creating a new order
            THEN Created order should be returned with correct data
            """)
    void shouldCreateOrder() {
        //GIVEN
        var user = prepareUser();
        var products = prepareProducts();
        var input = new OrderInput(products, ORDER_QUANTITY);
        var orderEntity = buildEntity();
        var expectedOrder = buildResponse(ORDER_ID_NEW, ORDER_STATUS, products);

        when(productRepository.findById(products.getFirst().getId())).thenReturn(Optional.of(products.getFirst()));
        when(productRepository.findById(products.getLast().getId())).thenReturn(Optional.of(products.getLast()));
        when(userRepository.findByName(USER_NAME)).thenReturn(Optional.of(user));
        when(orderRepository.save(any(DbOrder.class))).thenReturn(orderEntity);
        when(orderMapper.dbToDto(orderEntity)).thenReturn(expectedOrder);

        //WHEN
        var actualOrder = orderService.createOrder(user.getName(), input);

        //THEN
        assertThat(actualOrder).isNotNull()
                .satisfies(order -> {
                    assertThat(order.id()).isEqualTo(expectedOrder.id());
                    assertThat(order.status()).isEqualTo(expectedOrder.status());
                    assertThat(order.products().getFirst().getId()).isEqualTo(products.getFirst().getId());
                    assertThat(order.products()).hasSize(products.size());
                    assertThat(order.products()).containsExactlyInAnyOrderElementsOf(products);
                });

        verify(productRepository, times(products.size())).findById(any());
        verify(userRepository, times(1)).findByName(USER_NAME);
        verify(orderMapper, times(1)).dbToDto(orderEntity);
        verify(orderRepository, times(1)).save(any(DbOrder.class));
        verifyNoMoreInteractions(productRepository, userRepository, orderRepository, orderMapper);
    }

    @Test
    @DisplayName("""
            GIVEN Input for creating order
            WHEN Products don't exist
            THEN Should throw an exception
            """)
    void shouldThrowException_whenProductsNotFound() {
        //GIVEN
        var input = new OrderInput(prepareProducts(), ORDER_QUANTITY);

        //WHEN
        when(productRepository.findById(anyLong())).thenAnswer(_ -> Optional.empty());

        //THEN
        assertThatThrownBy(() -> orderService.createOrder(USER_NAME, input))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageContaining("No valid products found");
    }

    @Test
    @DisplayName("""
            GIVEN Input for creating order
            WHEN User doesn't exist
            THEN Should throw an exception
            """)
    void shouldThrowException_whenUserNotFound() {
        //GIVEN
        var input = new OrderInput(prepareProducts(), ORDER_QUANTITY);

        when(productRepository.findById(anyLong())).thenAnswer(_ -> Optional.of(mock(DbProduct.class)));

        //WHEN
        when(userRepository.findByName(USER_NAME)).thenReturn(Optional.empty());

        //THEN
        assertThatThrownBy(() -> orderService.createOrder(USER_NAME, input))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("User with username " + USER_NAME + " not found");
    }

    @Test
    @DisplayName("""
            GIVEN List of the orders for user
            WHEN Fetching the orders
            THEN Should return all orders for user
            """)
    void shouldGetOrdersByUsername() {
        //GIVEN
        var orderEntities = buildEntities();
        var expectedOrders = buildResponses();

        when(orderRepository.findByUserName(USER_NAME)).thenReturn(orderEntities);
        when(orderMapper.dbToDto(orderEntities.getFirst())).thenReturn(expectedOrders.getFirst());
        when(orderMapper.dbToDto(orderEntities.getLast())).thenReturn(expectedOrders.getLast());

        //WHEN
        var actualOrders = orderService.getOrdersByUsername(USER_NAME);

        //THEN
        assertThat(actualOrders)
                .hasSize(expectedOrders.size())
                .containsExactlyInAnyOrderElementsOf(expectedOrders);

        assertThat(actualOrders.getFirst())
                .satisfies(order -> {
                    assertThat(order.id()).isEqualTo(expectedOrders.getFirst().id());
                    assertThat(order.status()).isEqualTo(expectedOrders.getFirst().status());
                    assertThat(order.products()).hasSize(expectedOrders.getFirst().products().size());
                    assertThat(order.products()).containsExactlyInAnyOrderElementsOf(expectedOrders.getFirst().products());
                    assertThat(order.products().getFirst().getId()).isEqualTo(expectedOrders.getFirst().products().getFirst().getId());
                    assertThat(order.products().getFirst().getName()).isEqualTo(expectedOrders.getFirst().products().getFirst().getName());
                    assertThat(order.products().getFirst().getDescription()).isEqualTo(expectedOrders.getFirst().products().getFirst().getDescription());
                    assertThat(order.products().getFirst().getPrice()).isEqualTo(expectedOrders.getFirst().products().getFirst().getPrice());
                });

        verify(orderRepository, times(1)).findByUserName(USER_NAME);
        verify(orderMapper, times(orderEntities.size())).dbToDto(any());
        verifyNoMoreInteractions(orderRepository, orderMapper);
    }

    @Test
    @DisplayName("""
            GIVEN Existing Order
            WHEN Updating status of an existing order
            THEN Updated order should be returned with correct data
            """)
    void shouldUpdateOrderStatus() {
        //GIVEN
        var orderEntity = buildEntity();
        var expectedOrder = buildResponse(ORDER_ID_1, ORDER_STATUS_NEW, prepareProducts());

        when(orderRepository.findById(orderEntity.getId())).thenReturn(Optional.of(orderEntity));
        when(orderRepository.save(orderEntity)).thenReturn(orderEntity);
        when(orderMapper.dbToDto(orderEntity)).thenReturn(expectedOrder);

        //WHEN
        var actualOrder = orderService.updateOrderStatus(orderEntity.getId(), ORDER_STATUS_NEW);

        //THEN
        assertThat(actualOrder).isNotNull()
                .satisfies(order -> {
                    assertThat(order.id()).isEqualTo(expectedOrder.id());
                    assertThat(order.status()).isEqualTo(expectedOrder.status());
                    assertThat(order.products()).hasSize(expectedOrder.products().size());
                    assertThat(order.products()).containsExactlyInAnyOrderElementsOf(expectedOrder.products());
                    assertThat(order.products().getFirst().getId()).isEqualTo(expectedOrder.products().getFirst().getId());
                    assertThat(order.products().getFirst().getName()).isEqualTo(expectedOrder.products().getFirst().getName());
                    assertThat(order.products().getFirst().getDescription()).isEqualTo(expectedOrder.products().getFirst().getDescription());
                    assertThat(order.products().getFirst().getPrice()).isEqualTo(expectedOrder.products().getFirst().getPrice());
                });

        verify(orderRepository, times(1)).findById(orderEntity.getId());
        verify(orderRepository, times(1)).save(orderEntity);
        verify(orderMapper, times(1)).dbToDto(orderEntity);
        verify(notificationService, times(1))
                .sendOrderStatusChangeNotification(orderEntity.getUser().getEmail(), orderEntity.getId(), ORDER_STATUS_NEW);
        verifyNoMoreInteractions(orderRepository, orderMapper, notificationService);
    }

    @Test
    @DisplayName("""
            WHEN Order doesn't exist
            THEN Should throw an exception
            """)
    void shouldThrowException_whenOrderNotFound() {
        //WHEN
        when(orderRepository.findById(ORDER_ID_1)).thenReturn(Optional.empty());

        //THEN
        assertThatThrownBy(() -> orderService.updateOrderStatus(ORDER_ID_1, ORDER_STATUS_NEW))
                .isInstanceOf(OrderNotFoundException.class)
                .hasMessageContaining("Order with id #" + ORDER_ID_1 + " not found");
    }

    @Test
    @DisplayName("""
            GIVEN Existing Order
            WHEN Deleting an existing order
            THEN Should delete order
            """)
    void shouldDeleteOrder() {
        //GIVEN
        var orderEntity = buildEntity();

        when(orderRepository.existsById(orderEntity.getId())).thenReturn(true);
        doNothing().when(orderRepository).deleteById(orderEntity.getId());

        //WHEN
        orderService.deleteOrder(orderEntity.getId());

        //THEN
        verify(orderRepository, times(1)).existsById(orderEntity.getId());
        verify(orderRepository, times(1)).deleteById(orderEntity.getId());
        verifyNoMoreInteractions(orderRepository);
    }

    @Test
    @DisplayName("""
            WHEN Order doesn't exist
            THEN Should throw an exception
            """)
    void shouldThrowException_whenOrderDoesNotExist() {
        //WHEN
        when(orderRepository.existsById(ORDER_ID_1)).thenReturn(false);

        //THEN
        assertThatThrownBy(() -> orderService.deleteOrder(ORDER_ID_1))
                .isInstanceOf(OrderNotFoundException.class)
                .hasMessageContaining("Order with id #" + ORDER_ID_1 + " not found");
    }

    private DbUser prepareUser() {
        return new DbUser(USER_NAME, USER_PASSWORD, USER_ROLE, USER_EMAIL);
    }

    private List<DbProduct> prepareProducts() {
        return List.of(
                new DbProduct(PRODUCT_ID_1, PRODUCT_NAME_1, PRODUCT_DESCRIPTION, PRODUCT_PRICE_1),
                new DbProduct(PRODUCT_ID_2, PRODUCT_NAME_2, PRODUCT_DESCRIPTION, PRODUCT_PRICE_2)
        );
    }

    private DbOrder buildEntity(Long id, DbUser user, List<DbProduct> products) {
        return new DbOrder(id, user, products, ORDER_QUANTITY, ORDER_STATUS);
    }

    private DbOrder buildEntity() {
        return buildEntity(ORDER_ID_1, prepareUser(), prepareProducts());
    }

    private List<DbOrder> buildEntities () {
        return List.of(
                buildEntity(ORDER_ID_1, prepareUser(), prepareProducts()),
                buildEntity(ORDER_ID_2, prepareUser(), prepareProducts()));
    }

    private OrderDto buildResponse(Long id, OrderStatus status, List<DbProduct> products) {
        return new OrderDto(id, status, products);
    }

    private List<OrderDto> buildResponses() {
        return List.of(
                buildResponse(ORDER_ID_1, ORDER_STATUS, prepareProducts()),
                buildResponse(ORDER_ID_2, ORDER_STATUS, prepareProducts())
        );
    }
}
