package com.example.productapi.domain.product.dto;

public record ProductCreateRequest(
        String name,
        Integer price,
        Integer stock
) {
}