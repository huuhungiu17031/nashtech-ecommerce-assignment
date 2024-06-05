package com.nashtech.cellphonesfake.service.impl;

import com.nashtech.cellphonesfake.constant.Error;
import com.nashtech.cellphonesfake.enumeration.PaymentMethod;
import com.nashtech.cellphonesfake.enumeration.StatusType;
import com.nashtech.cellphonesfake.exception.BadRequestException;
import com.nashtech.cellphonesfake.exception.NotFoundException;
import com.nashtech.cellphonesfake.model.Order;
import com.nashtech.cellphonesfake.model.OrderDetail;
import com.nashtech.cellphonesfake.model.Product;
import com.nashtech.cellphonesfake.repository.OrderRepository;
import com.nashtech.cellphonesfake.service.OrderDetailService;
import com.nashtech.cellphonesfake.service.OrderService;
import com.nashtech.cellphonesfake.service.ProductService;
import com.nashtech.cellphonesfake.view.OrderAndOrderDetail;
import com.nashtech.cellphonesfake.view.OrderDetailVm;
import com.nashtech.cellphonesfake.view.OrderVm;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderDetailService orderDetailService;
    private final ProductService productService;

    public OrderServiceImpl(OrderRepository orderRepository, OrderDetailService orderDetailService, ProductService productService) {
        this.orderRepository = orderRepository;
        this.orderDetailService = orderDetailService;
        this.productService = productService;
    }

    @Transactional
    @Override
    public Long createOrder(OrderVm orderVm) {
        Long totalSize = 0L;
        for (OrderDetailVm orderDetailVm : orderVm.orderDetailVms()) {
            totalSize += orderDetailVm.amount();
        }
        if (orderVm.orderDetailVms().isEmpty()) throw new BadRequestException(Error.Message.NO_PRODUCTS_FOUND);
        if (totalSize > 10) throw new BadRequestException(Error.Message.TOO_MANY_PRODUCTS);

        Order order = null;
        if (orderVm.id() == 0) {
            order = new Order();
        } else {
            order = findOrderById(orderVm.id());
        }
        AtomicLong totalMoney = new AtomicLong(0L);
        List<OrderDetail> orderDetailsWithoutCart = orderVm.orderDetailVms().stream().map(orderDetailVm -> {
            Product product = productService.findProductById(orderDetailVm.productId());
            totalMoney.addAndGet((product.getPrice() * orderDetailVm.amount()));
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setProduct(product);
            orderDetail.setQuantity(orderDetailVm.amount());
            return orderDetail;
        }).toList();
        order.setTotalMoney(totalMoney.get());
        order.setPaymentMethod(orderVm.paymentMethod());
        order.setStatus(StatusType.PENDING);
        Order updatedOrder = orderRepository.save(order);
        for (OrderDetail orderDetail : orderDetailsWithoutCart) {
            orderDetail.setOrder(updatedOrder);
            orderDetailService.saveOrderDetail(orderDetail);
        }
        return updatedOrder.getId();
    }

    @Override
    public Order findOrderById(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new NotFoundException(String.format(Error.Message.RESOURCE_NOT_FOUND_BY_ID, "Order", id)));
    }

    @Transactional
    @Override
    public void save(Order order) {
        orderRepository.save(order);
    }

    @Transactional
    @Override
    public void updateOrder(Long id, StatusType status) {
        Order order = findOrderById(id);
        if (order.getPaymentMethod().equals(PaymentMethod.SHIP_CODE) && order.getStatus().equals(StatusType.PENDING) && status.equals(StatusType.COMPLETED)) {
            orderDetailService.findByOrderId(id).forEach(orderDetail -> {
                Product product = orderDetail.getProduct();
                product.setStockQuantity(product.getStockQuantity() - orderDetail.getQuantity());
                productService.saveProduct(product);
            });
            order.setStatus(status);
            orderRepository.save(order);
            return;
        }
        order.setStatus(status);
        orderRepository.save(order);
    }

    @Override
    public List<OrderAndOrderDetail> findOrderByEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return orderRepository.findAllByCreatedBy((String) authentication.getPrincipal()).stream().map(order -> {
            List<OrderDetailVm> orderDetailVms = order.getOrderDetailList().stream()
                    .map(orderDetail -> new OrderDetailVm(orderDetail.getProduct().getId(), orderDetail.getQuantity())).toList();
            return new OrderAndOrderDetail(order, orderDetailVms);
        }).toList();
    }
}
    