package com.nashtech.cellphonesfake.model;

import com.nashtech.cellphonesfake.enumeration.StatusType;
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
    @Enumerated(EnumType.STRING)
    StatusType status;
    @ManyToOne
    @JoinColumn(name = "user_id")
    User userOrder;
    @OneToMany(mappedBy = "order")
    List<OrderDetail> orderDetailList;
}
