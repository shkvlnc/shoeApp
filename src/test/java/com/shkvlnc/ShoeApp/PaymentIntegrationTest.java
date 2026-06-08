package com.shkvlnc.ShoeApp;

import com.shkvlnc.ShoeApp.entity.*;
import com.shkvlnc.ShoeApp.repository.OrderRepository;
import com.shkvlnc.ShoeApp.repository.PaymentRepository;
import com.shkvlnc.ShoeApp.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class PaymentsIntegrationTest {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderRepository orderRepository;

    private Order testOrder;

    @BeforeEach
    void setUp() {
        // Fresh order for each test
        testOrder = Order.builder()
                .userId(1L) // seeded testuser
                .productId(1L) // seeded product
                .quantity(1)
                .status(OrderStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        testOrder = orderRepository.save(testOrder);
    }

    @Test
    void testPaymentSuccessUpdatesOrder() {
        Payment payment = new Payment();
        payment.setOrderId(testOrder.getId());
        payment.setAmount(BigDecimal.valueOf(1999.99));
        payment.setMethod(PaymentMethod.CARD);
        payment.setStatus(PaymentStatus.PENDING);
        paymentRepository.save(payment);

        paymentService.updateStatus(payment.getId(), PaymentStatus.SUCCESS);

        Payment updatedPayment = paymentRepository.findById(payment.getId()).orElseThrow();
        assertThat(updatedPayment.getStatus()).isEqualTo(PaymentStatus.SUCCESS);

        Order updatedOrder = orderRepository.findById(testOrder.getId()).orElseThrow();
        assertThat(updatedOrder.getStatus()).isEqualTo(OrderStatus.PAID);
    }

    @Test
    void testPaymentFailureKeepsOrderPending() {
        Payment payment = new Payment();
        payment.setOrderId(testOrder.getId());
        payment.setAmount(BigDecimal.valueOf(1999.99));
        payment.setMethod(PaymentMethod.CARD);
        payment.setStatus(PaymentStatus.PENDING);
        paymentRepository.save(payment);

        paymentService.updateStatus(payment.getId(), PaymentStatus.FAILED);

        Payment updatedPayment = paymentRepository.findById(payment.getId()).orElseThrow();
        assertThat(updatedPayment.getStatus()).isEqualTo(PaymentStatus.FAILED);

        Order updatedOrder = orderRepository.findById(testOrder.getId()).orElseThrow();
        assertThat(updatedOrder.getStatus()).isEqualTo(OrderStatus.PENDING);
    }

    @Test
    void testRefundDoesNotChangeOrderPaidStatus() {
        Payment payment = new Payment();
        payment.setOrderId(testOrder.getId());
        payment.setAmount(BigDecimal.valueOf(1999.99));
        payment.setMethod(PaymentMethod.CARD);
        payment.setStatus(PaymentStatus.PENDING);
        paymentRepository.save(payment);

        paymentService.updateStatus(payment.getId(), PaymentStatus.SUCCESS);

        // Refund
        paymentService.updateStatus(payment.getId(), PaymentStatus.REFUNDED);

        Payment refundedPayment = paymentRepository.findById(payment.getId()).orElseThrow();
        assertThat(refundedPayment.getStatus()).isEqualTo(PaymentStatus.REFUNDED);

        Order updatedOrder = orderRepository.findById(testOrder.getId()).orElseThrow();
        assertThat(updatedOrder.getStatus()).isEqualTo(OrderStatus.PAID);
    }
}
