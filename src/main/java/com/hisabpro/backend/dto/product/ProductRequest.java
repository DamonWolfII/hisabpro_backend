package com.hisabpro.backend.dto.product;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProductRequest(

        @NotBlank(message = "Product name is required")
        String name,

        String sku,

        String unit,

        @NotNull(message = "Purchase price is required")
        @DecimalMin(value = "0.0", message = "Purchase price cannot be negative")
        BigDecimal purchasePrice,

        @NotNull(message = "Selling price is required")
        @DecimalMin(value = "0.0", message = "Selling price cannot be negative")
        BigDecimal sellingPrice
) {
}