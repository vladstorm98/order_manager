package com.order_manager.service;

import com.order_manager.dto.OrderRequest;
import com.order_manager.dto.OrderResponse;
import com.order_manager.entity.OrderEntity;
import com.order_manager.entity.OrderStatus;
import com.order_manager.entity.ProductEntity;
import com.order_manager.entity.UserEntity;
import com.order_manager.exception.OrderNotFoundException;
import com.order_manager.exception.UserNotFoundException;
import com.order_manager.mapper.OrderMapper;
import com.order_manager.repository.OrderRepository;
import com.order_manager.repository.ProductRepository;
import com.order_manager.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final NotificationService notificationService;
    private final OrderMapper orderMapper;

    @Transactional
    public List<OrderResponse> getAllOrdersForUser(String username) {
        return orderRepository.findByUserName(username)
                .stream()
                .map(orderMapper::toResponse)
                .toList();
    }

    public OrderResponse createOrder(String username, OrderRequest request) {
        UserEntity user = userRepository.findByName(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        List<ProductEntity> products = productRepository.findByIdIn(request.listOfProductId());
        if (products.isEmpty()) {
            throw new EntityNotFoundException("No valid products found");
        }

        OrderEntity order = OrderEntity.builder()
                .user(user)
                .products(products)
                .quantity(request.quantity())
                .status(OrderStatus.PENDING)
                .build();

        return orderMapper.toResponse(orderRepository.save(order));
    }

    @Transactional
    public OrderResponse updateOrderStatus(Long orderId, OrderStatus status) {
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));
        order.setStatus(status);

        notificationService.sendOrderStatusChangeNotification(order.getUser().getEmail(), orderId, status);

        return orderMapper.toResponse(orderRepository.save(order));
    }

    public void deleteOrder(Long orderId) {
        if (orderRepository.existsById(orderId)) {
            orderRepository.deleteById(orderId);
        } else {
            throw new EntityNotFoundException("Order not found");
        }
    }
}
