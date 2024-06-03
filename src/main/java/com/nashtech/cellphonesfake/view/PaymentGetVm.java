package com.nashtech.cellphonesfake.view;

public record PaymentGetVm(
        Long orderId,
        String amount,
        String bankCode,
        String cardType,
        String orderInfo,
        String payDate,
        String responseCode,
        String tmnCode,
        String transactionNo,
        String transactionStatus,
        String txnRef,
        String secureHash,
        String bankTranNo
) {
}
