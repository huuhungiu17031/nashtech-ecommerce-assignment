package com.nashtech.cellphonesfake.view;

import jakarta.validation.constraints.NotNull;

public record BrandPostVm(@NotNull String name, @NotNull String imagePath) {
}
