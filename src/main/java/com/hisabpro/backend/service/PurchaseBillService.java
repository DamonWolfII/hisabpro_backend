package com.hisabpro.backend.service;

import com.hisabpro.backend.dto.purchase.PurchaseBillItemRequest;
import com.hisabpro.backend.dto.purchase.PurchaseBillItemResponse;
import com.hisabpro.backend.dto.purchase.PurchaseBillRequest;
import com.hisabpro.backend.dto.purchase.PurchaseBillResponse;
import com.hisabpro.backend.entity.*;
import com.hisabpro.backend.repository.InventoryRepository;
import com.hisabpro.backend.repository.ProductRepository;
import com.hisabpro.backend.repository.PurchaseBillRepository;
import com.hisabpro.backend.dto.common.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
public class PurchaseBillService {

    private final PurchaseBillRepository purchaseBillRepository;
    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;
    private final CurrentUserService currentUserService;

    public PurchaseBillService(
            PurchaseBillRepository purchaseBillRepository,
            ProductRepository productRepository,
            InventoryRepository inventoryRepository,
            CurrentUserService currentUserService
    ) {
        this.purchaseBillRepository = purchaseBillRepository;
        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
        this.currentUserService = currentUserService;
    }

    @Transactional
    public PurchaseBillResponse create(
            PurchaseBillRequest request
    ) {

        User user = currentUserService.getCurrentUser();

        UUID companyId = user.getCompany().getId();

        // Check duplicate bill number
        if (purchaseBillRepository.existsByBillNumberAndCompanyId(
                request.billNumber(),
                companyId
        )) {
            throw new RuntimeException(
                    "Purchase bill with this bill number already exists"
            );
        }

        // Prevent the same product from appearing twice
        Set<UUID> productIds = new HashSet<>();

        for (PurchaseBillItemRequest item : request.items()) {

            if (!productIds.add(item.productId())) {
                throw new RuntimeException(
                        "Product cannot appear more than once in the same bill: "
                                + item.productId()
                );
            }
        }

        PurchaseBill purchaseBill = new PurchaseBill();

        purchaseBill.setBillNumber(request.billNumber());
        purchaseBill.setSupplierName(request.supplierName());
        purchaseBill.setCompanyId(companyId);

        List<PurchaseBillItem> billItems = new ArrayList<>();

        BigDecimal billTotal = BigDecimal.ZERO;

        for (PurchaseBillItemRequest itemRequest : request.items()) {

            Product product =
                    productRepository
                            .findByIdAndCompanyId(
                                    itemRequest.productId(),
                                    companyId
                            )
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Product not found: "
                                                    + itemRequest.productId()
                                    )
                            );

            BigDecimal itemTotal =
                    itemRequest.purchasePrice()
                            .multiply(
                                    BigDecimal.valueOf(
                                            itemRequest.quantity()
                                    )
                            );

            PurchaseBillItem billItem =
                    new PurchaseBillItem();

            billItem.setPurchaseBill(purchaseBill);
            billItem.setProduct(product);
            billItem.setQuantity(itemRequest.quantity());
            billItem.setPurchasePrice(
                    itemRequest.purchasePrice()
            );
            billItem.setTotalAmount(itemTotal);

            billItems.add(billItem);

            billTotal = billTotal.add(itemTotal);

            /*
             * Find existing inventory or create it.
             */
            Inventory inventory =
                    inventoryRepository
                            .findByProductIdAndCompanyId(
                                    product.getId(),
                                    companyId
                            )
                            .orElseGet(() -> {

                                Inventory newInventory =
                                        new Inventory();

                                newInventory.setProduct(product);
                                newInventory.setCompanyId(companyId);
                                newInventory.setQuantity(0);
                                newInventory.setLowStockThreshold(5);

                                return newInventory;
                            });

            /*
             * Increase stock.
             */
            inventory.setQuantity(
                    inventory.getQuantity()
                            + itemRequest.quantity()
            );

            inventoryRepository.save(inventory);
        }

        purchaseBill.setItems(billItems);
        purchaseBill.setTotalAmount(billTotal);

        PurchaseBill savedBill =
                purchaseBillRepository.save(purchaseBill);

        return toResponse(savedBill);
    }


    @Transactional(readOnly = true)
    public PageResponse<PurchaseBillResponse> getAll(
            int page,
            int limit
    ) {

        User user = currentUserService.getCurrentUser();

        UUID companyId = user.getCompany().getId();

        PageRequest pageable =
                PageRequest.of(page - 1, limit);

        Page<PurchaseBill> bills =
                purchaseBillRepository.findAllByCompanyId(
                        companyId,
                        pageable
                );

        return new PageResponse<>(
                bills.getContent()
                        .stream()
                        .map(this::toResponse)
                        .toList(),

                bills.getTotalElements(),

                page,

                limit
        );
    }


    private PurchaseBillResponse toResponse(
            PurchaseBill bill
    ) {

        List<PurchaseBillItemResponse> items =
                bill.getItems()
                        .stream()
                        .map(item ->
                                new PurchaseBillItemResponse(
                                        item.getId(),
                                        item.getProduct().getId(),
                                        item.getProduct().getName(),
                                        item.getProduct().getSku(),
                                        item.getQuantity(),
                                        item.getPurchasePrice(),
                                        item.getTotalAmount()
                                )
                        )
                        .toList();

        return new PurchaseBillResponse(
                bill.getId(),
                bill.getBillNumber(),
                bill.getSupplierName(),
                bill.getTotalAmount(),
                items,
                bill.getCreatedAt()
        );
    }
}

