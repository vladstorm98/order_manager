package com.order_manager.controller;

import com.order_manager.dto.OrderRequest;
import com.order_manager.dto.OrderResponse;
import com.order_manager.entity.OrderStatus;
import com.order_manager.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@Tag(name = "Orders")
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    @Operation(summary = "Get orders for a user")
    public List<OrderResponse> getAllOrdersForUser(@AuthenticationPrincipal UserDetails userDetails) {
        return orderService.getAllOrdersForUser(userDetails.getUsername());
    }

    @PostMapping
    @Operation(summary = "Create a new order")
    public OrderResponse createOrder(@AuthenticationPrincipal UserDetails userDetails,
                                     @RequestBody OrderRequest request) {
        return orderService.createOrder(userDetails.getUsername(), request);
    }

    @PutMapping("/{orderId}")
    @Operation(summary = "Update status of the order by ID")
    public OrderResponse updateOrderStatus(@Parameter(description = "Type ID of the order to be updated")
                                           @PathVariable Long orderId) {
        return orderService.updateOrderStatus(orderId, OrderStatus.COMPLETED);
    }

    @DeleteMapping("/{orderId}")
    @Operation(summary = "Delete the order by ID")
    public void deleteOrder(@Parameter(description = "Type ID of the order to be deleted")
                            @PathVariable Long orderId) {
        orderService.deleteOrder(orderId);
    }
}
