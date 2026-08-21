package com.hisabpro.backend.service;

import com.hisabpro.backend.dto.common.PageResponse;
import com.hisabpro.backend.dto.inventory.InventoryRequest;
import com.hisabpro.backend.dto.inventory.InventoryResponse;
import com.hisabpro.backend.entity.Inventory;
import com.hisabpro.backend.entity.Product;
import com.hisabpro.backend.entity.User;
import com.hisabpro.backend.repository.InventoryRepository;
import com.hisabpro.backend.repository.ProductRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;
    private final CurrentUserService currentUserService;

    public InventoryService(
            InventoryRepository inventoryRepository,
            ProductRepository productRepository,
            CurrentUserService currentUserService
    ) {
        this.inventoryRepository = inventoryRepository;
        this.productRepository = productRepository;
        this.currentUserService = currentUserService;
    }

    public InventoryResponse create(
            InventoryRequest request
    ) {

        User user = currentUserService.getCurrentUser();

        UUID companyId = user.getCompany().getId();

        Product product =
                productRepository
                        .findByIdAndCompanyId(
                                request.productId(),
                                companyId
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Product not found"
                                )
                        );

        if (inventoryRepository.existsByProductIdAndCompanyId(
                request.productId(),
                companyId
        )) {

            throw new RuntimeException(
                    "Inventory already exists for this product"
            );
        }

        Inventory inventory = new Inventory();

        inventory.setProduct(product);
        inventory.setCompanyId(companyId);
        inventory.setQuantity(request.quantity());
        inventory.setLowStockThreshold(
                request.lowStockThreshold()
        );

        return toResponse(
                inventoryRepository.save(inventory)
        );
    }

    public PageResponse<InventoryResponse> getAll(
            int page,
            int limit
    ) {

        User user = currentUserService.getCurrentUser();

        UUID companyId = user.getCompany().getId();

        PageRequest pageable =
                PageRequest.of(page - 1, limit);

        Page<Inventory> inventories =
                inventoryRepository.findAllByCompanyId(
                        companyId,
                        pageable
                );

        return new PageResponse<>(
                inventories
                        .getContent()
                        .stream()
                        .map(this::toResponse)
                        .toList(),

                inventories.getTotalElements(),

                page,

                limit
        );
    }

    public InventoryResponse getById(UUID id) {

        User user = currentUserService.getCurrentUser();

        Inventory inventory =
                inventoryRepository
                        .findByIdAndCompanyId(
                                id,
                                user.getCompany().getId()
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Inventory not found"
                                )
                        );

        return toResponse(inventory);
    }

    private InventoryResponse toResponse(
            Inventory inventory
    ) {

        Product product = inventory.getProduct();

        return new InventoryResponse(
                inventory.getId(),
                product.getId(),
                product.getName(),
                product.getSku(),
                product.getUnit(),
                inventory.getQuantity(),
                inventory.getLowStockThreshold(),
                product.getPurchasePrice(),
                product.getSellingPrice(),
                inventory.getQuantity()
                        <= inventory.getLowStockThreshold()
        );
    }
}