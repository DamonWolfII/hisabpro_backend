package com.hisabpro.backend.service;

import com.hisabpro.backend.dto.common.PageResponse;
import com.hisabpro.backend.dto.product.ProductRequest;
import com.hisabpro.backend.dto.product.ProductResponse;
import com.hisabpro.backend.entity.Product;
import com.hisabpro.backend.entity.User;
import com.hisabpro.backend.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CurrentUserService currentUserService;

    public ProductService(
            ProductRepository productRepository,
            CurrentUserService currentUserService
    ) {
        this.productRepository = productRepository;
        this.currentUserService = currentUserService;
    }

    public ProductResponse create(ProductRequest request) {

        User user = currentUserService.getCurrentUser();

        UUID companyId = user.getCompany().getId();

        if (request.sku() != null &&
                productRepository.existsBySkuAndCompanyId(
                        request.sku(),
                        companyId
                )) {

            throw new RuntimeException(
                    "Product with this SKU already exists"
            );
        }

        Product product = new Product();

        product.setName(request.name());
        product.setSku(request.sku());
        product.setUnit(request.unit());
        product.setPurchasePrice(request.purchasePrice());
        product.setSellingPrice(request.sellingPrice());
        product.setCompanyId(companyId);
        product.setActive(true);

        return toResponse(
                productRepository.save(product)
        );
    }

    public PageResponse<ProductResponse> getAll(
            int page,
            int limit
    ) {

        User user = currentUserService.getCurrentUser();

        PageRequest pageable =
                PageRequest.of(page - 1, limit);

        Page<Product> products =
                productRepository.findAllByCompanyId(
                        user.getCompany().getId(),
                        pageable
                );

        return new PageResponse<>(
                products.getContent()
                        .stream()
                        .map(this::toResponse)
                        .toList(),

                products.getTotalElements(),

                page,

                limit
        );
    }

    public ProductResponse getById(UUID id) {

        User user = currentUserService.getCurrentUser();

        Product product =
                productRepository
                        .findByIdAndCompanyId(
                                id,
                                user.getCompany().getId()
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Product not found"
                                )
                        );

        return toResponse(product);
    }

    private ProductResponse toResponse(Product product) {

        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getSku(),
                product.getUnit(),
                product.getPurchasePrice(),
                product.getSellingPrice(),
                product.isActive()
        );
    }
}