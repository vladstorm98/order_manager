package com.order_manager.service;

import com.order_manager.dto.OrderRequest;
import com.order_manager.dto.OrderResponse;
import com.order_manager.entity.OrderEntity;
import com.order_manager.entity.OrderStatus;
import com.order_manager.entity.ProductEntity;
import com.order_manager.entity.UserEntity;
import com.order_manager.repository.OrderRepository;
import com.order_manager.repository.ProductRepository;
import com.order_manager.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final NotificationService notificationService;


    public OrderResponse createOrder(String username, List<Long> listOfProductId, int quantity) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        List<ProductEntity> products = productRepository.findByIdIn(listOfProductId);
        if (products.isEmpty()) {
            throw new EntityNotFoundException("No valid products found");
        }

        OrderEntity order = OrderEntity.builder()
                .user(user)
                .products(products)
                .quantity(quantity)
                .status(OrderStatus.PENDING)
                .build();

        return mapToResponse(orderRepository.save(order));
    }

    @Transactional
    public List<OrderResponse> getOrdersForUser(String username) {
        return orderRepository.findByUserUsername(username)
                .stream()
                .map(OrderResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse updateOrderStatus(Long orderId, OrderStatus status) {
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
        order.setStatus(status);

        notificationService.sendOrderStatusChangeNotification(order.getUser().getEmail(), orderId, status);

        return mapToResponse(orderRepository.save(order));
    }

    public void deleteOrder(Long orderId) {
        if (orderRepository.existsById(orderId)) {
            orderRepository.deleteById(orderId);
        } else {
            throw new EntityNotFoundException("Order not found");
        }
    }

    public OrderResponse mapToResponse(OrderEntity order) {
        return new OrderResponse(order);
    }
}

