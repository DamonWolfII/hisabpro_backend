package com.hisabpro.backend.repository;

import com.hisabpro.backend.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository
        extends JpaRepository<Product, UUID> {

    Page<Product> findAllByCompanyId(
            UUID companyId,
            Pageable pageable
    );

    Optional<Product> findByIdAndCompanyId(
            UUID id,
            UUID companyId
    );

    boolean existsBySkuAndCompanyId(
            String sku,
            UUID companyId
    );
}