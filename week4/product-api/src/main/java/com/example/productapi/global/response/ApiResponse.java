package com.example.productapi.global.response;

public record ApiResponse<T>(
        boolean success,
        T data
) {
}