package com.nashtech.cellphonesfake.view;

import jakarta.validation.constraints.NotBlank;

public record ProductCardVm(
        Long id,
        @NotBlank String productName,
        @NotBlank Double price,
        String thumbnail
) {
}
