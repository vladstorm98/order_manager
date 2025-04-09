package com.order_manager.service;

import com.order_manager.dto.OrderDTO;
import com.order_manager.dto.OrderInput;
import com.order_manager.entity.DbOrder;
import com.order_manager.entity.OrderStatus;
import com.order_manager.entity.DbUser;
import com.order_manager.exception.OrderNotFoundException;
import com.order_manager.exception.ProductNotFoundException;
import com.order_manager.exception.UserNotFoundException;
import com.order_manager.mapper.OrderMapper;
import com.order_manager.repository.OrderRepository;
import com.order_manager.repository.ProductRepository;
import com.order_manager.repository.UserRepository;
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

    public OrderDTO createOrder(String username, OrderInput input) {

        input.products().forEach(product -> {
            if (productRepository.findById(product.getId()).isEmpty()) {
                throw new ProductNotFoundException("No valid products found");
            }
        });

        DbUser user = userRepository.findByName(username)
                .orElseThrow(() -> new UserNotFoundException("User with username " + username + " not found"));

        DbOrder order = new DbOrder(user, input.products(), input.quantity(), OrderStatus.PENDING);

        OrderDTO response = orderMapper.dbToDto(orderRepository.save(order));

        log.info("Order with id #{} was created", response.id());
        return response;
    }

    @Transactional
    public List<OrderDTO> getAllOrdersForUser(String username) {
        List<OrderDTO> response = orderRepository.findByUserName(username)
                .stream()
                .map(orderMapper::dbToDto)
                .toList();

        log.info("Orders were successfully retrieved");
        return response;
    }

    @Transactional
    public OrderDTO updateOrderStatus(@NonNull Long id, OrderStatus status) {
        DbOrder order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order with id #" + id + " not found"));
        order.setStatus(status);

        notificationService.sendOrderStatusChangeNotification(order.getUser().getEmail(), id, status);

        OrderDTO response = orderMapper.dbToDto(orderRepository.save(order));

        log.info("Order with id #{} was updated", id);
        return response;
    }

    public void deleteOrder(@NonNull Long id) {
        if (orderRepository.existsById(id)) {
            orderRepository.deleteById(id);
            log.info("Order with id #{} was deleted", id);
        } else {
            throw new OrderNotFoundException("Order with id #" + id + " not found");
        }
    }
}
