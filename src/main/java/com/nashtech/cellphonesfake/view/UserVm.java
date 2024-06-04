package com.nashtech.cellphonesfake.view;

import java.util.List;

public record UserVm(
        Long id,
        String email,
        List<String> roles,
        Boolean action,
        Boolean isBlock
) {
}
