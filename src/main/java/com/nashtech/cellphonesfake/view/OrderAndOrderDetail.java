package com.nashtech.cellphonesfake.view;

import com.nashtech.cellphonesfake.model.Order;

import java.util.List;

public record OrderAndOrderDetail(Order order, List<OrderDetailVm> orderDetailVm) {
}
