package com.nashtech.cellphonesfake.controller;

import com.nashtech.cellphonesfake.service.PaymentService;
import com.nashtech.cellphonesfake.view.PaymentGetVm;
import com.nashtech.cellphonesfake.view.PaymentResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/vnPay")
@Slf4j
public class VnPayController {
    private final PaymentService paymentService;

    public VnPayController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping
    public ResponseEntity<PaymentResponse> createPayment(HttpServletRequest request, @RequestParam Long orderId) {
        PaymentResponse paymentResponse = paymentService.createPayment(request, orderId);
        return new ResponseEntity<>(paymentResponse, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentResponse> checkPaymentAndUpdateOrder(
            @PathVariable Long id,
            @RequestParam("vnp_Amount") String amount,
            @RequestParam("vnp_BankCode") String bankCode,
            @RequestParam("vnp_CardType") String cardType,
            @RequestParam("vnp_OrderInfo") String orderInfo,
            @RequestParam("vnp_PayDate") String payDate,
            @RequestParam("vnp_ResponseCode") String responseCode,
            @RequestParam("vnp_TmnCode") String tmnCode,
            @RequestParam("vnp_TransactionNo") String transactionNo,
            @RequestParam("vnp_TransactionStatus") String transactionStatus,
            @RequestParam("vnp_TxnRef") String txnRef,
            @RequestParam("vnp_SecureHash") String secureHash,
            @RequestParam(name = "vnp_BankTranNo", defaultValue = "") String bankTranNo
    ) {
        PaymentGetVm paymentGetVm = new PaymentGetVm(id, amount, bankCode, cardType, orderInfo, payDate, responseCode, tmnCode, transactionNo, transactionStatus, txnRef, secureHash, bankTranNo);
        return new ResponseEntity<>(paymentService.getPayment(paymentGetVm), HttpStatus.OK);
    }
}
