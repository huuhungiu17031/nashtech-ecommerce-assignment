package com.nashtech.cellphonesfake.controller;

import com.nashtech.cellphonesfake.service.OrderDetailService;

import com.nashtech.cellphonesfake.view.OrderDetailVm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/order-detail")
public class OrderDetailController {
    private final OrderDetailService orderDetailService;

    public OrderDetailController(OrderDetailService orderDetailService) {
        this.orderDetailService = orderDetailService;
    }

    @PostMapping
    public void createOrderDetail(@RequestBody List<OrderDetailVm> orderDetailVms) {
        log.info("Creating order detail {}", orderDetailVms);
    }
}
