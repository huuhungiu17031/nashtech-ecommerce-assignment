package com.nashtech.cellphonesfake.view;

import jakarta.validation.constraints.NotBlank;

public record ProductDetailVm(
        Long id,
        @NotBlank String productName,
        @NotBlank Double price,
        String description
) {

}
