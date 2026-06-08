package com.shkvlnc.ShoeApp.service;

import com.shkvlnc.ShoeApp.dto.PaymentRequestDTO;
import com.shkvlnc.ShoeApp.dto.PaymentResponseDTO;
import com.shkvlnc.ShoeApp.entity.Payment;
import com.shkvlnc.ShoeApp.entity.PaymentStatus;
import com.shkvlnc.ShoeApp.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    public PaymentResponseDTO initiatePayment(PaymentRequestDTO request) {
        Payment payment = new Payment();
        payment.setOrderId(request.orderId());
        payment.setMethod(request.method());
        payment.setAmount(request.amount());
        payment.setStatus(PaymentStatus.PENDING);
        payment.setTimestamp(LocalDateTime.now());

        return toResponse(paymentRepository.save(payment));
    }

    public PaymentResponseDTO updateStatus(Long id, PaymentStatus status) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found with id " + id));
        payment.setStatus(status);
        return toResponse(paymentRepository.save(payment));
    }

    public List<PaymentResponseDTO> getPaymentsByOrder(Long orderId) {
        return paymentRepository.findByOrderId(orderId)
                .stream().map(this::toResponse).toList();
    }

    private PaymentResponseDTO toResponse(Payment payment) {
        return new PaymentResponseDTO(payment.getId(), payment.getOrderId(),
                payment.getStatus(), payment.getMethod(),
                payment.getAmount(), payment.getTimestamp());
    }
}

