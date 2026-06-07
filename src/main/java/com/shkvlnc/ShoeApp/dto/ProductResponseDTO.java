package com.shkvlnc.ShoeApp.dto;

import lombok.*;

@Getter
@Setter
public class ProductResponseDTO {
    private Long id;
    private String name;
    private Double price;
    private Integer stock;

    public ProductResponseDTO(Long id, String name, Double price, Integer stock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    // getters and setters
}
