package com.nashtech.cellphonesfake.service.impl;

import com.nashtech.cellphonesfake.model.OrderDetail;
import com.nashtech.cellphonesfake.model.Product;
import com.nashtech.cellphonesfake.repository.OrderDetailRepository;
import com.nashtech.cellphonesfake.service.OrderDetailService;
import com.nashtech.cellphonesfake.service.ProductService;
import com.nashtech.cellphonesfake.view.OrderDetailVm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class OrderDetailServiceImpl implements OrderDetailService {
    private final OrderDetailRepository orderDetailRepository;
    private final ProductService productService;

    public OrderDetailServiceImpl(OrderDetailRepository orderDetailRepository, ProductService productService) {
        this.productService = productService;
        this.orderDetailRepository = orderDetailRepository;
    }

    @Override
    public List<OrderDetail> createOrderDetail(List<OrderDetailVm> orderDetailVms) {
       List<OrderDetail> list = orderDetailVms.stream().map(orderDetailVm -> {
            OrderDetail orderDetail = new OrderDetail();
            Product product = productService.findProductById(orderDetailVm.productId());
            orderDetail.setProduct(product);
            return orderDetail;
        }).toList();
        return List.of();
    }

    @Override
    public OrderDetail saveOrderDetail(OrderDetail orderDetail) {
        return orderDetailRepository.save(orderDetail);
    }

    @Override
    public List<OrderDetail> findByOrderId(Long orderId) {
        return orderDetailRepository.findByOrder_Id(orderId);
    }
}
