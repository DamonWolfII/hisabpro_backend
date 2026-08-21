package com.hisabpro.backend.controller;

import com.hisabpro.backend.dto.common.PageResponse;
import com.hisabpro.backend.dto.inventory.InventoryRequest;
import com.hisabpro.backend.dto.inventory.InventoryResponse;
import com.hisabpro.backend.service.InventoryService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(
            InventoryService inventoryService
    ) {
        this.inventoryService = inventoryService;
    }

    @PostMapping
    public ResponseEntity<InventoryResponse> create(
            @Valid @RequestBody InventoryRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(inventoryService.create(request));
    }

    @GetMapping
    public ResponseEntity<PageResponse<InventoryResponse>> getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int limit
    ) {

        return ResponseEntity.ok(
                inventoryService.getAll(page, limit)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventoryResponse> getById(
            @PathVariable UUID id
    ) {

        return ResponseEntity.ok(
                inventoryService.getById(id)
        );
    }
}