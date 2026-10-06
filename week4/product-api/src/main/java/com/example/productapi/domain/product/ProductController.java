package com.example.productapi.domain.product;

import com.example.productapi.domain.product.dto.ProductCreateRequest;
import com.example.productapi.domain.product.dto.ProductResponse;
import com.example.productapi.domain.product.dto.ProductUpdateRequest;
import com.example.productapi.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ApiResponse<Long>> create(
            @RequestBody ProductCreateRequest request
    ) {
        Long productId = productService.create(request);

        return ResponseEntity.ok(
                new ApiResponse<>(true, productId)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProduct(
            @PathVariable Long id
    ) {
        ProductResponse response = productService.getProduct(id);

        ApiResponse<ProductResponse> apiResponse =
                new ApiResponse<>(true, response);

        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ProductResponse>>> getProducts(
            Pageable pageable
    ) {
        Page<ProductResponse> response = productService.getProducts(pageable);

        return ResponseEntity.ok(
                new ApiResponse<>(true, response)
        );
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateProduct(
            @PathVariable Long id,
            @RequestBody ProductUpdateRequest request
    ) {
        productService.updateProduct(id, request);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable Long id
    ) {
        productService.deleteProduct(id);

        return ResponseEntity.noContent().build();
    }
}