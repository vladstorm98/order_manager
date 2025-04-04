package com.order_manager.service;

import com.order_manager.dto.OrderRequest;
import com.order_manager.dto.OrderResponse;
import com.order_manager.entity.OrderEntity;
import com.order_manager.entity.OrderStatus;
import com.order_manager.entity.UserEntity;
import com.order_manager.exception.OrderNotFoundException;
import com.order_manager.exception.UserNotFoundException;
import com.order_manager.mapper.OrderMapper;
import com.order_manager.repository.OrderRepository;
import com.order_manager.repository.ProductRepository;
import com.order_manager.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.NonNull;
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

    public OrderResponse createOrder(String username, OrderRequest request) {

        request.products().forEach(product -> {
            if (productRepository.findById(product.getId()).isEmpty()) {
                throw new EntityNotFoundException("No valid products found");
            }
        });

        UserEntity user = userRepository.findByName(username)
                .orElseThrow(() -> new UserNotFoundException("User with username " + username + " not found"));

        OrderEntity order = new OrderEntity(user, request.products(), request.quantity(), OrderStatus.PENDING);

        OrderResponse response = orderMapper.toResponse(orderRepository.save(order));

        log.info("Order with id #{} was created", response.id());
        return response;
    }

    @Transactional
    public List<OrderResponse> getAllOrdersForUser(String username) {
        List<OrderResponse> response = orderRepository.findByUserName(username)
                .stream()
                .map(orderMapper::toResponse)
                .toList();

        log.info("Orders were successfully retrieved");
        return response;
    }

    @Transactional
    public OrderResponse updateOrderStatus(@NonNull Long id, OrderStatus status) {
        OrderEntity order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order with id #" + id + " not found"));
        order.setStatus(status);

        notificationService.sendOrderStatusChangeNotification(order.getUser().getEmail(), id, status);

        OrderResponse response = orderMapper.toResponse(orderRepository.save(order));

        log.info("Order with id #{} was updated", id);
        return response;
    }

    public void deleteOrder(@NonNull Long id) {
        if (orderRepository.existsById(id)) {
            orderRepository.deleteById(id);
            log.info("Order with id #{} was deleted", id);
        } else {
            throw new EntityNotFoundException("Order with id #" + id + " not found");
        }
    }
}
