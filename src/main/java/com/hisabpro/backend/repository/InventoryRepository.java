package com.hisabpro.backend.repository;

import com.hisabpro.backend.entity.Inventory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface InventoryRepository extends JpaRepository<Inventory, UUID> {
    @Query(""" 
            SELECT i FROM Inventory i JOIN FETCH i.product WHERE i.companyId = :companyId 
            """)
    Page<Inventory> findAllByCompanyId(@Param("companyId") UUID companyId, Pageable pageable);

    @Query(""" 
            SELECT i FROM Inventory i JOIN FETCH i.product WHERE i.id = :id AND i.companyId = :companyId 
            """)
    Optional<Inventory> findByIdAndCompanyId(@Param("id") UUID id, @Param("companyId") UUID companyId);

    @Query(""" 
            SELECT i FROM Inventory i JOIN FETCH i.product WHERE i.product.id = :productId AND i.companyId = :companyId 
            """)
    Optional<Inventory> findByProductIdAndCompanyId(@Param("productId") UUID productId, @Param("companyId") UUID companyId);

    boolean existsByProductIdAndCompanyId(UUID productId, UUID companyId);
}