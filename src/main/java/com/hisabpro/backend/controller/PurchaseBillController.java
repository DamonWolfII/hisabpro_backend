package com.hisabpro.backend.controller;

import com.hisabpro.backend.dto.purchase.PurchaseBillRequest;
import com.hisabpro.backend.dto.purchase.PurchaseBillResponse;
import com.hisabpro.backend.service.PurchaseBillService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/purchases")
public class PurchaseBillController {

    private final PurchaseBillService purchaseBillService;

    public PurchaseBillController(
            PurchaseBillService purchaseBillService
    ) {
        this.purchaseBillService = purchaseBillService;
    }

    @PostMapping
    public ResponseEntity<PurchaseBillResponse> create(
            @Valid @RequestBody PurchaseBillRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        purchaseBillService.create(request)
                );
    }
}