package com.nashtech.cellphonesfake.view;

import jakarta.validation.constraints.NotNull;

public record BrandVm(
        Long id,
        @NotNull String name,
        String imagePath
) {}
