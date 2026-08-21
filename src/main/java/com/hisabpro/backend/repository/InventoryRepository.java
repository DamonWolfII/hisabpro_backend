package com.hisabpro.backend.repository;

import com.hisabpro.backend.entity.Inventory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface InventoryRepository
        extends JpaRepository<Inventory, UUID> {

    Page<Inventory> findAllByCompanyId(
            UUID companyId,
            Pageable pageable
    );

    Optional<Inventory> findByIdAndCompanyId(
            UUID id,
            UUID companyId
    );

    Optional<Inventory> findByProductIdAndCompanyId(
            UUID productId,
            UUID companyId
    );

    boolean existsByProductIdAndCompanyId(
            UUID productId,
            UUID companyId
    );
}