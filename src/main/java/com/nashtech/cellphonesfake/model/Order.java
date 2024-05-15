package com.nashtech.cellphonesfake.model;

import com.nashtech.cellphonesfake.enumeration.StatusType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
