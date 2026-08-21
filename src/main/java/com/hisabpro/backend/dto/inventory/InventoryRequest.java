package com.hisabpro.backend.dto.inventory;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record InventoryRequest(

        @NotNull(message = "Product ID is required")
        UUID productId,

        @Min(value = 0, message = "Quantity cannot be negative")
        int quantity,

        @Min(value = 0, message = "Low stock threshold cannot be negative")
        int lowStockThreshold
) {
}