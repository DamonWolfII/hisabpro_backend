package com.hisabpro.backend.dto.purchase;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record PurchaseBillRequest(

        @NotBlank(message = "Bill number is required")
        String billNumber,

        @NotBlank(message = "Supplier name is required")
        String supplierName,

        @NotEmpty(message = "Purchase bill must contain at least one item")
        @Valid
        List<PurchaseBillItemRequest> items
) {
}