package com.nashtech.cellphonesfake.service;

import com.nashtech.cellphonesfake.enumeration.StatusType;
import com.nashtech.cellphonesfake.model.Order;
import com.nashtech.cellphonesfake.view.OrderAndOrderDetail;
import com.nashtech.cellphonesfake.view.OrderVm;

import java.util.List;

public interface OrderService {
    Long createOrder(OrderVm orderVm);
    Order findOrderById(Long id);
    void save(Order order);
    void updateOrder(Long order, StatusType status);
    List<OrderAndOrderDetail> findOrderByEmail();
}
