package com.shkvlnc.ShoeApp.controller;

import com.shkvlnc.ShoeApp.dto.OrderRequestDTO;
import com.shkvlnc.ShoeApp.dto.OrderResponseDTO;
import com.shkvlnc.ShoeApp.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // ✅ USER: Place an order
    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public OrderResponseDTO placeOrder(@RequestParam Long userId,
                                       @Valid @RequestBody OrderRequestDTO dto) {
        return orderService.placeOrder(userId, dto);
    }

    // ✅ USER: Get own orders
    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public List<OrderResponseDTO> getUserOrders(@RequestParam Long userId) {
        return orderService.getUserOrders(userId);
    }

    // ✅ ADMIN: Get all orders
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public List<OrderResponseDTO> getAllOrders() {
        return orderService.getAllOrders();
    }

    // ✅ ADMIN: Update order status
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public OrderResponseDTO updateOrderStatus(@PathVariable Long id,
                                              @RequestBody Map<String, String> body) {
        return orderService.updateOrderStatus(id, body.get("status"));
    }

    // ✅ ADMIN: Delete order
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
    }
}
