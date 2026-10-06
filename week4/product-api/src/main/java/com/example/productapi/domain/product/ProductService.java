package com.example.productapi.domain.product;

import com.example.productapi.domain.product.dto.ProductCreateRequest;
import com.example.productapi.domain.product.dto.ProductResponse;
import com.example.productapi.domain.product.dto.ProductUpdateRequest;
import com.example.productapi.domain.product.exception.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    public Long create(ProductCreateRequest request) {

        Product product = new Product(
                request.name(),
                request.price(),
                request.stock()
        );

        Product savedProduct = productRepository.save(product);

        return savedProduct.getId();
    }

    @Transactional(readOnly = true)
    public ProductResponse getProduct(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(ProductNotFoundException::new);

        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getStock()
        );
    }

    @Transactional(readOnly = true)
    public Page<ProductResponse> getProducts(Pageable pageable) {

        return productRepository.findAll(pageable)
                .map(product -> new ProductResponse(
                        product.getId(),
                        product.getName(),
                        product.getPrice(),
                        product.getStock()
                ));
    }

    @Transactional
    public void updateProduct(
            Long id,
            ProductUpdateRequest request
    ) {
        Product product = productRepository.findById(id)
                .orElseThrow(ProductNotFoundException::new);

        product.update(
                request.name(),
                request.price(),
                request.stock()
        );
    }

    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(ProductNotFoundException::new);

        productRepository.delete(product);
    }
}