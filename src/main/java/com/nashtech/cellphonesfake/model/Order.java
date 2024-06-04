package com.nashtech.cellphonesfake.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nashtech.cellphonesfake.enumeration.PaymentMethod;
import com.nashtech.cellphonesfake.enumeration.StatusType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity(name = "orders")
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
    @Enumerated(EnumType.STRING)
    private StatusType status = StatusType.PENDING;
    private Long totalMoney;
    @OneToMany(mappedBy = "order")
    @JsonIgnore
    private List<OrderDetail> orderDetailList;
}
