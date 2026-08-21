package com.hisabpro.backend.repository;

import com.hisabpro.backend.entity.PurchaseBillItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PurchaseBillItemRepository
        extends JpaRepository<PurchaseBillItem, UUID> {
}