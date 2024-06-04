package com.nashtech.cellphonesfake.view;

import java.time.Instant;

public record CategoryAdminVm(
        Long id,
        String categoryName,
        String categoryDescription,
        String icon,
        Boolean isPublished,
        Instant createdAt, String createdBy, Instant updatedAt, String updatedBy
) {
}