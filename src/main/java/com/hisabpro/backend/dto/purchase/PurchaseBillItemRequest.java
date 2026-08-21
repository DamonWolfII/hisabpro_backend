package com.hisabpro.backend.dto.purchase;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record PurchaseBillItemRequest(

        @NotNull(message = "Product ID is required")
        UUID productId,

        @Min(value = 1, message = "Quantity must be at least 1")
        int quantity,

        @NotNull(message = "Purchase price is required")
        @DecimalMin(
                value = "0.0",
                inclusive = false,
                message = "Purchase price must be greater than 0"
        )
        BigDecimal purchasePrice
) {
}