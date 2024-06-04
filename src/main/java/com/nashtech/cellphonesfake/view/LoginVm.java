package com.nashtech.cellphonesfake.view;

import jakarta.validation.constraints.NotBlank;

public record LoginVm(@NotBlank String email, @NotBlank String password) {
}
