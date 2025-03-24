package com.order_manager.service;

import com.order_manager.dto.OrderRequest;
import com.order_manager.dto.OrderResponse;
import com.order_manager.entity.OrderEntity;
import com.order_manager.entity.OrderStatus;
import com.order_manager.entity.ProductEntity;
import com.order_manager.entity.UserEntity;
import com.order_manager.exception.OrderNotFoundException;
import com.order_manager.exception.UserNotFoundException;
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

    public List<OrderResponse> getAllOrdersForUser(String username) {
        List<OrderResponse> response = orderRepository.findByUserUsername(username)
                .stream()
                .map(this::mapToResponse)
                .toList();

        log.info("Orders were successfully retrieved");
        return response;
    }

    public OrderResponse createOrder(String name, OrderRequest request) {
        UserEntity user = userRepository.findByUsername(name)
                .orElseThrow(() -> new UserNotFoundException("User with username " + name + " not found"));

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

        OrderResponse response = mapToResponse(orderRepository.save(order));

        log.info("Order with id #{} was created", response.id());
        return response;
    }

    @Transactional
    public OrderResponse updateOrderStatus(Long id, OrderStatus status) {
        OrderEntity order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order with id #" + id + " not found"));
        order.setStatus(status);

        notificationService.sendOrderStatusChangeNotification(order.getUser().getEmail(), id, status);

        OrderResponse response = mapToResponse(orderRepository.save(order));

        log.info("Order with id #{} was updated", id);
        return response;
    }

    public void deleteOrder(Long id) {
        if (orderRepository.existsById(id)) {
            orderRepository.deleteById(id);
            log.info("Order with id #{} was deleted", id);
        } else {
            throw new EntityNotFoundException("Order with id #" + id + " not found");
        }
    }

    private OrderResponse mapToResponse(OrderEntity order) {
        return new OrderResponse(order);
    }
}

