package com.nashtech.cellphonesfake.view;

public record ProductAdminVm(
        Long id,
        String productName,
        Long price,
        Long stockQuantity,
        Boolean isFeatured,
        String thumbnail
) {
}
