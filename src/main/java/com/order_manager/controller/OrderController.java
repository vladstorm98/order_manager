package com.order_manager.controller;

import com.order_manager.dto.OrderRequest;
import com.order_manager.dto.OrderResponse;
import com.order_manager.entity.OrderStatus;
import com.order_manager.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public List<OrderResponse> getOrdersForUser(@AuthenticationPrincipal UserDetails userDetails) {
        return orderService.getOrdersForUser(userDetails.getUsername());
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@AuthenticationPrincipal UserDetails userDetails,
                                                     @RequestBody OrderRequest request) {
        OrderResponse order = orderService.createOrder(userDetails.getUsername(), request);

        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @PutMapping("/{orderId}")
    public OrderResponse updateOrderStatus(@PathVariable Long orderId) {
        return orderService.updateOrderStatus(orderId, OrderStatus.COMPLETED);
    }

    @DeleteMapping("/{orderId}")
    public void deleteOrder(@PathVariable Long orderId) {
        orderService.deleteOrder(orderId);
    }
}
