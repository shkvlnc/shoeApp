package com.shkvlnc.ShoeApp.service;

import com.shkvlnc.ShoeApp.dto.OrderRequestDTO;
import com.shkvlnc.ShoeApp.dto.OrderResponseDTO;
import com.shkvlnc.ShoeApp.entity.Order;
import com.shkvlnc.ShoeApp.entity.OrderStatus;
import com.shkvlnc.ShoeApp.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    // ✅ Create
    public OrderResponseDTO placeOrder(Long userId, OrderRequestDTO dto) {
        Order order = buildOrder(userId, dto);
        return toResponse(orderRepository.save(order));
    }

    // ✅ Read (User)
    public List<OrderResponseDTO> getUserOrders(Long userId) {
        return orderRepository.findByUserId(userId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ✅ Read (Admin)
    public List<OrderResponseDTO> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ✅ Update
    public OrderResponseDTO updateOrderStatus(Long id, String status) {
        Order order = findOrderById(id);
        order.setStatus(OrderStatus.valueOf(status.toUpperCase()));
        return toResponse(orderRepository.save(order));
    }

    // ✅ Delete
    public void deleteOrder(Long id) {
        Order order = findOrderById(id);
        orderRepository.delete(order);
    }

    // 🔒 Private Helpers
    private Order buildOrder(Long userId, OrderRequestDTO dto) {
        return Order.builder()
                .userId(userId)
                .productId(dto.getProductId())
                .quantity(dto.getQuantity())
                .status(OrderStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();
    }

    private Order findOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with id " + id));
    }

    private OrderResponseDTO toResponse(Order order) {
        return OrderResponseDTO.builder()
                .id(order.getId())
                .productId(order.getProductId())
                .quantity(order.getQuantity())
                .status(order.getStatus().name())
                .createdAt(order.getCreatedAt())
                .build();
    }
}
