package com.shkvlnc.ShoeApp.service;

import com.shkvlnc.ShoeApp.dto.PaymentRequestDTO;
import com.shkvlnc.ShoeApp.dto.PaymentResponseDTO;
import com.shkvlnc.ShoeApp.entity.Order;
import com.shkvlnc.ShoeApp.entity.OrderStatus;
import com.shkvlnc.ShoeApp.entity.Payment;
import com.shkvlnc.ShoeApp.entity.PaymentStatus;
import com.shkvlnc.ShoeApp.repository.OrderRepository;
import com.shkvlnc.ShoeApp.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderRepository orderRepository; // ✅ injected

    public PaymentResponseDTO initiatePayment(PaymentRequestDTO request) {
        Payment payment = new Payment();
        payment.setOrderId(request.orderId());
        payment.setMethod(request.method());
        payment.setAmount(request.amount());
        payment.setStatus(PaymentStatus.PENDING);
        payment.setTimestamp(LocalDateTime.now());

        return toResponse(paymentRepository.save(payment));
    }

    @Transactional
    public PaymentResponseDTO updateStatus(Long id, PaymentStatus status) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found with id " + id));
        payment.setStatus(status);
        Payment saved = paymentRepository.save(payment);

        // ✅ Reflect in Orders when payment succeeds
        if (status == PaymentStatus.SUCCESS) {
            Order order = orderRepository.findById(payment.getOrderId())
                    .orElseThrow(() -> new IllegalArgumentException("Order not found"));
            order.setStatus(OrderStatus.PAID);
            orderRepository.save(order);
        }

        return toResponse(saved);
    }

    public List<PaymentResponseDTO> getPaymentsByOrder(Long orderId) {
        return paymentRepository.findByOrderId(orderId)
                .stream().map(this::toResponse).toList();
    }

    // ✅ Keep this helper method inside the class
    private PaymentResponseDTO toResponse(Payment payment) {
        return new PaymentResponseDTO(payment.getId(), payment.getOrderId(),
                payment.getStatus(), payment.getMethod(),
                payment.getAmount(), payment.getTimestamp());
    }
}
