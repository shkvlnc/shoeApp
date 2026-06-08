package com.shkvlnc.ShoeApp.dto;

import com.shkvlnc.ShoeApp.entity.PaymentMethod;
import com.shkvlnc.ShoeApp.entity.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentResponseDTO(Long id, Long orderId, PaymentStatus status,
                                 PaymentMethod method, BigDecimal amount,
                                 LocalDateTime timestamp) {}