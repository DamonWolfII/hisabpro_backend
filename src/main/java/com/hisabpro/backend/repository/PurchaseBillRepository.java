package com.hisabpro.backend.repository;

import com.hisabpro.backend.entity.PurchaseBill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PurchaseBillRepository
        extends JpaRepository<PurchaseBill, UUID> {

    Page<PurchaseBill> findAllByCompanyId(
            UUID companyId,
            Pageable pageable
    );

    Optional<PurchaseBill> findByIdAndCompanyId(
            UUID id,
            UUID companyId
    );

    boolean existsByBillNumberAndCompanyId(
            String billNumber,
            UUID companyId
    );
}