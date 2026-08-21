package com.hisabpro.backend.controller;

import com.hisabpro.backend.dto.product.ProductRequest;
import com.hisabpro.backend.dto.product.ProductResponse;
import com.hisabpro.backend.service.ProductService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hisabpro.backend.dto.common.PageResponse;

import java.util.UUID;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductResponse> create(
            @Valid @RequestBody ProductRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(productService.create(request));
    }

    @GetMapping
    public ResponseEntity<PageResponse<ProductResponse>> getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int limit
    ) {

        return ResponseEntity.ok(
                productService.getAll(page, limit)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getById(
            @PathVariable UUID id
    ) {

        return ResponseEntity.ok(
                productService.getById(id)
        );
    }
}