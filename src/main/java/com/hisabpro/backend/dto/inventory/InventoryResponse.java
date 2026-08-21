package com.hisabpro.backend.dto.inventory;

import java.math.BigDecimal;
import java.util.UUID;

public record InventoryResponse(
        UUID id,
        UUID productId,
        String productName,
        String sku,
        String unit,
        Integer quantity,
        Integer lowStockThreshold,
        BigDecimal purchasePrice,
        BigDecimal sellingPrice,
        boolean lowStock
) {
}