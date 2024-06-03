package com.nashtech.cellphonesfake.service;

import com.nashtech.cellphonesfake.model.OrderDetail;
import com.nashtech.cellphonesfake.view.OrderDetailVm;

import java.util.List;

public interface OrderDetailService {
    List<OrderDetail> createOrderDetail(List<OrderDetailVm> orderDetailVms);
    OrderDetail saveOrderDetail(OrderDetail orderDetail);
    List<OrderDetail> findByOrderId(Long orderId);
}
