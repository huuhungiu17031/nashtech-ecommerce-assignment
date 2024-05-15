package com.nashtech.cellphonesfake.view;

import jakarta.validation.constraints.NotBlank;

public record CartDetailRequest(@NotBlank Long productId, @NotBlank Integer amount) {
}
