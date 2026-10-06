package com.example.productapi.global.exception;

import com.example.productapi.domain.product.exception.ProductNotFoundException;
import com.example.productapi.global.response.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProductNotFoundException(
            ProductNotFoundException e
    ) {
        ErrorResponse response = new ErrorResponse(
                404,
                "PRODUCT_NOT_FOUND",
                e.getMessage()
        );

        return ResponseEntity
                .status(404)
                .body(response);
    }
}