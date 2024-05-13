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
@Entity(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String payment;
    String status;
    @ManyToOne
    @JoinColumn(name = "user_id")
    User userOrder;
    @OneToMany(mappedBy = "order")
    List<OrderDetail> orderDetailList;
}
