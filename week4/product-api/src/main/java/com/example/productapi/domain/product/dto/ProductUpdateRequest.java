package com.example.productapi.domain.product.dto;

public record ProductUpdateRequest(
        String name,
        Integer price,
        Integer stock
) {
}