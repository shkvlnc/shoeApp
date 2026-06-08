package com.shkvlnc.ShoeApp.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponseDTO {
    private Long id;
    private Long productId;
    private Integer quantity;
    private String status;
    private LocalDateTime createdAt;
}
