package com.nashtech.cellphonesfake.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity(name = "product_gallery")
public class ProductGallery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(length = 500)
    String imagePath;
    Boolean thumbnail = false;
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "product_id", nullable = false)
    Product product;
}
