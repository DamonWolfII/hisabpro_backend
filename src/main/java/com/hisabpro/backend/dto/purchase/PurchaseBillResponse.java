package com.hisabpro.backend.dto.purchase;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record PurchaseBillResponse(

        UUID id,

        String billNumber,

        String supplierName,

        BigDecimal totalAmount,

        List<PurchaseBillItemResponse> items,

        Instant createdAt
) {
}