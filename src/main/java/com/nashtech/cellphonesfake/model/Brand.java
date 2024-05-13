package com.nashtech.cellphonesfake.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity(name = "brand")
public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(unique = true)
    String name;
    String imagePath;
    @OneToMany(mappedBy = "brand", fetch = FetchType.LAZY)
    List<Product> products;

    @ManyToOne
    @JoinColumn(name = "category_id")
    Category category;
}
