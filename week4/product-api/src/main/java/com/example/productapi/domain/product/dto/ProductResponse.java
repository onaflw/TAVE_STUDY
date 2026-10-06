package com.example.productapi.domain.product.dto;

public record ProductResponse(
        Long id,
        String name,
        Integer price,
        Integer stock
) {
}