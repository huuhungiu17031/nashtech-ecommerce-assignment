package com.nashtech.cellphonesfake.controller;

import com.nashtech.cellphonesfake.enumeration.StatusType;
import com.nashtech.cellphonesfake.service.OrderService;
import com.nashtech.cellphonesfake.view.OrderVm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Long> createOrder(@RequestBody OrderVm orderVm) {
        Long id = orderService.createOrder(orderVm);
        return ResponseEntity.ok(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateOrder(@PathVariable Long id, @RequestParam StatusType statusType) {
        orderService.updateOrder(id, statusType);
        return new ResponseEntity<>("Order was updated successfully", HttpStatus.OK);
    }

}
