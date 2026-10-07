package com.example.productapi.domain.product;

import com.example.productapi.domain.product.dto.ProductCreateRequest;
import com.example.productapi.domain.product.dto.ProductResponse;
import com.example.productapi.domain.product.dto.ProductUpdateRequest;
import com.example.productapi.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Product",
        description = "상품 관리 API"
)
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    @Operation(
            summary = "상품 생성",
            description = "상품명, 가격, 재고를 입력하여 새로운 상품을 생성합니다."
    )
    @PostMapping
    public ResponseEntity<ApiResponse<Long>> create(
            @RequestBody ProductCreateRequest request
    ) {
        Long productId = productService.create(request);

        return ResponseEntity.ok(
                new ApiResponse<>(true, productId)
        );
    }

    @Operation(
            summary = "상품 단건 조회",
            description = "상품 ID를 이용하여 상품 정보를 조회합니다."
    )
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProduct(
            @PathVariable Long id
    ) {
        ProductResponse response = productService.getProduct(id);

        ApiResponse<ProductResponse> apiResponse =
                new ApiResponse<>(true, response);

        return ResponseEntity.ok(apiResponse);
    }

    @Operation(
            summary = "상품 페이지별로 조회"
    )
    @GetMapping
    public ResponseEntity<ApiResponse<Page<ProductResponse>>> getProducts(
            Pageable pageable
    ) {
        Page<ProductResponse> response = productService.getProducts(pageable);

        return ResponseEntity.ok(
                new ApiResponse<>(true, response)
        );
    }

    @Operation(
            summary = "상품 수정"
    )
    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateProduct(
            @PathVariable Long id,
            @RequestBody ProductUpdateRequest request
    ) {
        productService.updateProduct(id, request);

        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "상품 삭제"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable Long id
    ) {
        productService.deleteProduct(id);

        return ResponseEntity.noContent().build();
    }
}