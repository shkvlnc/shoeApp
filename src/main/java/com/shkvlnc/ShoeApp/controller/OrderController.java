package com.shkvlnc.ShoeApp.controller;

import com.shkvlnc.ShoeApp.entity.Order;
import com.shkvlnc.ShoeApp.entity.User;
import com.shkvlnc.ShoeApp.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public Order placeOrder(@RequestBody Order order) {
        return orderService.placeOrder(order);
    }

    @GetMapping("/user/{userId}")
    public List<Order> getOrdersByUser(@PathVariable Long userId) {
        User user = new User();
        user.setId(userId);
        return orderService.getOrdersByUser(user);
    }

    @PutMapping("/{orderId}/status")
    public Order updateStatus(@PathVariable Long orderId, @RequestParam String status) {
        return orderService.updateOrderStatus(orderId, status);
    }
}
