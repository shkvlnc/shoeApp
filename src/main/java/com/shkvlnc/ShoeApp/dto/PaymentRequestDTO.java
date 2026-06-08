package com.shkvlnc.ShoeApp.dto;

import com.shkvlnc.ShoeApp.entity.PaymentMethod;

import java.math.BigDecimal;

public record PaymentRequestDTO(Long orderId, PaymentMethod method, BigDecimal amount) {}