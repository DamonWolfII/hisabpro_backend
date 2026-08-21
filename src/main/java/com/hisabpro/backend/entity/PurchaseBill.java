package com.hisabpro.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(
        name = "purchase_bills",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_purchase_bill_company_number",
                        columnNames = {"company_id", "bill_number"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class PurchaseBill {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String billNumber;

    @Column(nullable = false)
    private UUID companyId;

    @Column(nullable = false)
    private String supplierName;

    @Column(nullable = false)
    private BigDecimal totalAmount;

    @OneToMany(
            mappedBy = "purchaseBill",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<PurchaseBillItem> items = new ArrayList<>();

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    private Instant updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }
}