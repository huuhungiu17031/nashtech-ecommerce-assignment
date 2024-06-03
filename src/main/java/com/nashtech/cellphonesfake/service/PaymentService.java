package com.nashtech.cellphonesfake.service;

import com.nashtech.cellphonesfake.view.PaymentGetVm;
import com.nashtech.cellphonesfake.view.PaymentResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestParam;

public interface PaymentService {
    PaymentResponse createPayment(HttpServletRequest request, @RequestParam Long orderId);

    PaymentResponse getPayment(PaymentGetVm paymentGetVm);
}
