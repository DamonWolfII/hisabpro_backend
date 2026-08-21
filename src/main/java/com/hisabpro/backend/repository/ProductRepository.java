package com.hisabpro.backend.repository;

import com.hisabpro.backend.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository
        extends JpaRepository<Product, UUID> {

    // List products for the company
    Page<Product> findAllByCompanyId(
            UUID companyId,
            Pageable pageable
    );

    // Search products by name
    Page<Product> findAllByCompanyIdAndNameContainingIgnoreCase(
            UUID companyId,
            String name,
            Pageable pageable
    );

    // Get a specific product belonging to the company
    Optional<Product> findByIdAndCompanyId(
            UUID id,
            UUID companyId
    );

    // Check SKU uniqueness inside the company
    boolean existsBySkuAndCompanyId(
            String sku,
            UUID companyId
    );
}