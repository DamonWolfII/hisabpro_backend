package com.hisabpro.backend.dto.purchase;

import java.math.BigDecimal;
import java.util.UUID;

public record PurchaseBillItemResponse(

        UUID id,

        UUID productId,

        String productName,

        String sku,

        Integer quantity,

        BigDecimal purchasePrice,

        BigDecimal totalAmount
) {
}