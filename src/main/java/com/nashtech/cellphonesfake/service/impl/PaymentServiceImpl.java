package com.nashtech.cellphonesfake.service.impl;

import com.nashtech.cellphonesfake.configuration.VnPayConfig;
import com.nashtech.cellphonesfake.enumeration.PaymentMethod;
import com.nashtech.cellphonesfake.enumeration.StatusType;
import com.nashtech.cellphonesfake.exception.BadRequestException;
import com.nashtech.cellphonesfake.model.Order;
import com.nashtech.cellphonesfake.model.Product;
import com.nashtech.cellphonesfake.service.OrderDetailService;
import com.nashtech.cellphonesfake.service.OrderService;
import com.nashtech.cellphonesfake.service.PaymentService;
import com.nashtech.cellphonesfake.service.ProductService;
import com.nashtech.cellphonesfake.view.PaymentGetVm;
import com.nashtech.cellphonesfake.view.PaymentResponse;
import com.nashtech.cellphonesfake.view.VnPayQueryAndSecureHash;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {
    private final OrderService orderService;
    private final OrderDetailService orderDetailService;
    private final ProductService productService;

    public PaymentServiceImpl(OrderService orderService, OrderDetailService orderDetailService, ProductService productService) {
        this.orderDetailService = orderDetailService;
        this.orderService = orderService;
        this.productService = productService;
    }

    @Override
    public PaymentResponse createPayment(HttpServletRequest request, Long orderId) {
        Order order = orderService.findOrderById(orderId);
        if (order.getStatus().equals(StatusType.COMPLETED)) return new PaymentResponse("Ok", "Successfully", null);
        String returnUrl = "http://localhost:5174/checkout/" + orderId;
        if (order.getPaymentMethod().equals(PaymentMethod.VN_PAY)) {
            long amount = order.getTotalMoney() * 100;
            Map<String, String> vnpParams = new HashMap<>();
            String vnpTxnRef = VnPayConfig.getRandomNumber(8);
            String vnpVersion = "2.1.0";
            String vnpCommand = "pay";
            vnpParams.put("vnp_Version", vnpVersion);
            vnpParams.put("vnp_Command", vnpCommand);
            vnpParams.put("vnp_TmnCode", VnPayConfig.VNP_TMN_CODE);
            vnpParams.put("vnp_Amount", String.valueOf(amount));
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            String vnpCreateDate = dateFormat.format(calendar.getTime());
            vnpParams.put("vnp_CreateDate", vnpCreateDate);
            vnpParams.put("vnp_CurrCode", "VND");
            vnpParams.put("vnp_IpAddr", VnPayConfig.getIpAddress(request));
            vnpParams.put("vnp_Locale", "vn");
            vnpParams.put("vnp_OrderInfo", "Thanh Toan Don Hang:" + order.getId());
            vnpParams.put("vnp_OrderType", "200000");
            vnpParams.put("vnp_ReturnUrl", returnUrl);
            vnpParams.put("vnp_TxnRef", vnpTxnRef);
            calendar.add(Calendar.MINUTE, 15);
            String vnpExpireDate = dateFormat.format(calendar.getTime());
            vnpParams.put("vnp_ExpireDate", vnpExpireDate);
            VnPayQueryAndSecureHash vnPayQueryAndSecureHash = createVnPayQueryAndSecureHash(vnpParams);
            String paymentUrl = VnPayConfig.VNP_PAY_URL + "?" +
                    vnPayQueryAndSecureHash.query() +
                    "&vnp_SecureHash=" +
                    vnPayQueryAndSecureHash.secureHash();
            return new PaymentResponse("Ok", "Processing Payment", paymentUrl);
        }
        throw new BadRequestException("Payment failed");
    }

    @Override
    public PaymentResponse getPayment(PaymentGetVm paymentGetVm) {
        Map<String, String> vnpParams = getStringStringMap(paymentGetVm);
        VnPayQueryAndSecureHash vnPayQueryAndSecureHash = createVnPayQueryAndSecureHash(vnpParams);
        String secureHash = vnPayQueryAndSecureHash.secureHash();
        if (paymentGetVm.secureHash().equalsIgnoreCase(secureHash)) {
            if (paymentGetVm.responseCode().equalsIgnoreCase("00"))
                return generatePayment(StatusType.COMPLETED, paymentGetVm.orderId(), "Paid successfully");
            if (paymentGetVm.responseCode().equalsIgnoreCase("01"))
                return generatePayment(StatusType.PENDING, paymentGetVm.orderId(), "Payment is not completed");
        }
        throw new BadRequestException("Payment failed");
    }

    private static Map<String, String> getStringStringMap(PaymentGetVm paymentGetVm) {
        Map<String, String> vnpParams = new HashMap<>();
        vnpParams.put("vnp_Amount", String.valueOf(paymentGetVm.amount()));
        vnpParams.put("vnp_BankCode", paymentGetVm.bankCode());

        if (!paymentGetVm.bankTranNo().equalsIgnoreCase("")) {
            vnpParams.put("vnp_BankTranNo", paymentGetVm.bankTranNo());
        }

        vnpParams.put("vnp_CardType", paymentGetVm.cardType());
        vnpParams.put("vnp_OrderInfo", paymentGetVm.orderInfo());
        vnpParams.put("vnp_PayDate", paymentGetVm.payDate());
        vnpParams.put("vnp_ResponseCode", paymentGetVm.responseCode());
        vnpParams.put("vnp_TmnCode", paymentGetVm.tmnCode());
        vnpParams.put("vnp_TransactionNo", paymentGetVm.transactionNo());
        vnpParams.put("vnp_TransactionStatus", paymentGetVm.transactionStatus());
        vnpParams.put("vnp_TxnRef", paymentGetVm.txnRef());
        return vnpParams;
    }


    private VnPayQueryAndSecureHash createVnPayQueryAndSecureHash(Map<String, String> vnpParams) {
        List<String> fieldNames = new ArrayList<>(vnpParams.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator<String> iterator = fieldNames.iterator();
        while (iterator.hasNext()) {
            String fieldName = iterator.next();
            String fieldValue = vnpParams.get(fieldName);
            if ((fieldValue != null) && (!fieldValue.isEmpty())) {
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                if (iterator.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String secureHash = VnPayConfig.hmacSHA512(VnPayConfig.VNP_HASH_SECRET, hashData.toString());
        return new VnPayQueryAndSecureHash(queryUrl, secureHash);
    }

    private PaymentResponse generatePayment(StatusType status, Long orderId, String message) {
        Order order = orderService.findOrderById(orderId);
        if (order.getStatus().equals(StatusType.PENDING) && status.equals(StatusType.COMPLETED)) {
            order.setStatus(StatusType.COMPLETED);
            orderDetailService.findByOrderId(orderId).forEach(orderDetail -> {
                Product product = orderDetail.getProduct();
                product.setStockQuantity(product.getStockQuantity() - orderDetail.getQuantity());
                productService.saveProduct(product);
            });
            orderService.save(order);
            return new PaymentResponse(status.toString(), message, null);
        }
        if (order.getStatus().equals(StatusType.COMPLETED)) return new PaymentResponse(status.toString(), message, null);
        order.setStatus(status);
        orderService.save(order);
        return new PaymentResponse(status.toString(), message, null);
    }
}
