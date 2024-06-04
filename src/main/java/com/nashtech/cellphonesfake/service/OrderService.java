package com.nashtech.cellphonesfake.service;

import com.nashtech.cellphonesfake.enumeration.StatusType;
import com.nashtech.cellphonesfake.model.Order;
import com.nashtech.cellphonesfake.view.OrderVm;

public interface OrderService {
    Long createOrder(OrderVm orderVm);
    Order findOrderById(Long id);
    void save(Order order);
    void updateOrder(Long order, StatusType status);
}
