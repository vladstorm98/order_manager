package com.order_manager.service;

import com.order_manager.dto.OrderDTO;
import com.order_manager.dto.OrderInput;
import com.order_manager.entity.DbOrder;
import com.order_manager.entity.DbUser;
import com.order_manager.entity.OrderStatus;
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

    public OrderDto createOrder(String username, OrderInput input) {
        input.products().forEach(product -> {
            if (productRepository.findById(product.getId()).isEmpty()) {
                throw new ProductNotFoundException("No valid products found");
            }
        });

        var dbUser = userRepository.findByName(username)
                .orElseThrow(() -> new UserNotFoundException("User '" + username + "' not found"));

        var dbOrder = createDbOrder(dbUser, input);
        var orderDto = saveAndConvertToDto(dbOrder);

        OrderDTO response = orderMapper.dbToDto(orderRepository.save(order));

        log.info("Order with id #{} was created", orderDto.id());
        return orderDto;
    }

    @Transactional
    public List<OrderDto> getOrdersByUsername(String username) {

        List<OrderDto> orderDto = orderRepository.findByUserName(username)
                .stream()
                .map(orderMapper::dbToDto)
                .toList();


        log.info("Orders retrieved from DB");
        return orderDto;
    }

    @Transactional
    public OrderDto updateOrderStatus(@NonNull Long id, OrderStatus status) {
        var dbOrder = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order with id #" + id + " not found"))
                .setStatus(status);

        var orderDto = saveAndConvertToDto(dbOrder);

        notificationService.sendOrderStatusChangeNotification(dbOrder.getUser().getEmail(), id, status);

        log.info("Order with id #{} was updated", id);
        return orderDto;
    }

    public void deleteOrder(@NonNull Long id) {
        var dbOrder = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order with id #" + id + " not found"));

        orderRepository.deleteById(id);

        log.info("Order with id #{} was deleted", id);
    }

    private DbOrder createDbOrder(DbUser user, OrderInput input) {
        return new DbOrder(user, input.products(), input.quantity(), OrderStatus.PENDING);
    }

    private OrderDto saveAndConvertToDto(DbOrder order) {
        return orderMapper.dbToDto(orderRepository.save(order));
    }
}
