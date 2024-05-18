package com.nashtech.cellphonesfake.view;

import com.nashtech.cellphonesfake.enumeration.ProductType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ProductPostVm(
        Long id,
        @NotNull String productName,
        @NotNull Long price,
        @Enumerated(EnumType.STRING)
        ProductType type,
        @NotNull Long stockQuantity,
        @NotNull String description,
        List<String> productImageUrls,
        Long brandId,
        Long categoryId
) {}
