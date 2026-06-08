package com.shkvlnc.ShoeApp.controller;

import com.shkvlnc.ShoeApp.dto.PaymentRequestDTO;
import com.shkvlnc.ShoeApp.dto.PaymentResponseDTO;
import com.shkvlnc.ShoeApp.entity.PaymentStatus;
import com.shkvlnc.ShoeApp.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<PaymentResponseDTO> initiatePayment(@RequestBody PaymentRequestDTO request) {
        return ResponseEntity.ok(paymentService.initiatePayment(request));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public ResponseEntity<List<PaymentResponseDTO>> getPaymentsByOrder(@RequestParam Long orderId) {
        return ResponseEntity.ok(paymentService.getPaymentsByOrder(orderId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/status")
    public ResponseEntity<PaymentResponseDTO> updateStatus(@PathVariable Long id,
                                                           @RequestParam PaymentStatus status) {
        return ResponseEntity.ok(paymentService.updateStatus(id, status));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable Long id) {
        paymentService.updateStatus(id, PaymentStatus.REFUNDED);
        return ResponseEntity.noContent().build();
    }
}
