package com.nashtech.cellphonesfake.view;

import com.nashtech.cellphonesfake.enumeration.PaymentMethod;

import java.util.List;

public record OrderVm(PaymentMethod paymentMethod, List<OrderDetailVm> orderDetailVms, Long id) {
}
