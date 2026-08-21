package com.hisabpro.backend.repository;

import com.hisabpro.backend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository
        extends JpaRepository<Product, UUID> {

    List<Product> findAllByCompanyId(UUID companyId);

    Optional<Product> findByIdAndCompanyId(
            UUID id,
            UUID companyId
    );

    boolean existsBySkuAndCompanyId(
            String sku,
            UUID companyId
    );
}