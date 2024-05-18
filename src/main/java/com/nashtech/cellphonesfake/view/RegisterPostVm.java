package com.nashtech.cellphonesfake.view;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterPostVm(@Email String email, @NotBlank String password) {
}
