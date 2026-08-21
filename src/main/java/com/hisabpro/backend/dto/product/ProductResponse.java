package com.hisabpro.backend.dto.product;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductResponse(
        UUID id,
        String name,
        String sku,
        String unit,
        BigDecimal purchasePrice,
        BigDecimal sellingPrice,
        boolean active
) {
}