package com.nashtech.cellphonesfake.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity(name = "cart_detail")
public class CartDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Long amount;
    @ManyToOne
    @JoinColumn (name = "cart_id")
    Cart cart;
    @ManyToOne
    @JoinColumn (name = "product_id")
    Product product;
}
