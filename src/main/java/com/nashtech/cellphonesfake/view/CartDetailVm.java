package com.nashtech.cellphonesfake.view;

import jakarta.validation.constraints.PositiveOrZero;

public record CartDetailVm(Long id, @PositiveOrZero Long amount) {
}
