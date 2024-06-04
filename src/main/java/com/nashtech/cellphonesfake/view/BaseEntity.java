package com.nashtech.cellphonesfake.view;

import java.time.Instant;

public record BaseEntity(Instant createdAt, String createdBy, Instant updatedAt, String updatedBy) {
}
