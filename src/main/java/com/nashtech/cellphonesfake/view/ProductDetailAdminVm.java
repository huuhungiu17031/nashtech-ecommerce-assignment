package com.nashtech.cellphonesfake.view;

public record ProductDetailAdminVm(
        Long id,
        String productName,
        Long price,
        Long stockQuantity,
        String description,
        Boolean isFeatured,
        Long brandId,
        Long categoryId
) {
}
