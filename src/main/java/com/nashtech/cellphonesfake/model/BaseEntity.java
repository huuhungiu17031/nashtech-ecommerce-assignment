package com.nashtech.cellphonesfake.model;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BaseEntity {

    @CreatedDate
    @Column(updatable = false)
    Instant createdAt;

    @CreatedBy
    @Column(updatable = false)
    String createdBy;

    @LastModifiedDate
    @Column(insertable = false)
    Instant updatedAt;

    @LastModifiedBy
    @Column(insertable = false)
    String updatedBy;
}
