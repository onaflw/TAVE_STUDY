package com.example.productapi.domain.product;

import com.example.productapi.domain.product.dto.ProductCreateRequest;
import com.example.productapi.domain.product.dto.ProductResponse;
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
    public ResponseEntity<Long> create(
            @RequestBody ProductCreateRequest request
    ) {
        Long productId = productService.create(request);

        return ResponseEntity.ok(productId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProduct(
            @PathVariable Long id
    ) {
        ProductResponse response = productService.getProduct(id);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<ProductResponse>> getProducts(
            Pageable pageable
    ) {
        Page<ProductResponse> response =
                productService.getProducts(pageable);

        return ResponseEntity.ok(response);
    }
}