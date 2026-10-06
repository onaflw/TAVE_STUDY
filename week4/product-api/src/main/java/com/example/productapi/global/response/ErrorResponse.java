package com.example.productapi.global.response;

public record ErrorResponse(
        int status,
        String code,
        String message
) {
}