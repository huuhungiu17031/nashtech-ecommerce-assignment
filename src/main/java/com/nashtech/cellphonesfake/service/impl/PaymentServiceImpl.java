package com.nashtech.cellphonesfake.service.impl;

import com.nashtech.cellphonesfake.configuration.VnPayConfig;
import com.nashtech.cellphonesfake.constant.Error;
import com.nashtech.cellphonesfake.constant.Message;
import com.nashtech.cellphonesfake.enumeration.PaymentMethod;
import com.nashtech.cellphonesfake.enumeration.StatusType;
import com.nashtech.cellphonesfake.exception.BadRequestException;
import com.nashtech.cellphonesfake.exception.NotFoundException;
import com.nashtech.cellphonesfake.model.Cart;
import com.nashtech.cellphonesfake.model.CartDetail;
import com.nashtech.cellphonesfake.model.Order;
import com.nashtech.cellphonesfake.model.Product;
import com.nashtech.cellphonesfake.repository.CartDetailRepository;
import com.nashtech.cellphonesfake.repository.CartRepository;
import com.nashtech.cellphonesfake.service.OrderDetailService;
import com.nashtech.cellphonesfake.service.OrderService;
import com.nashtech.cellphonesfake.service.PaymentService;
import com.nashtech.cellphonesfake.service.ProductService;
import com.nashtech.cellphonesfake.view.PaymentGetVm;
import com.nashtech.cellphonesfake.view.PaymentResponse;
import com.nashtech.cellphonesfake.view.VnPayQueryAndSecureHash;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {
    private final OrderService orderService;
    private final OrderDetailService orderDetailService;
    private final ProductService productService;
    private final CartRepository cartRepository;
    private final CartDetailRepository cartDetailRepository;
    public PaymentServiceImpl(
            OrderService orderService,
            OrderDetailService orderDetailService,
            ProductService productService,
            CartDetailRepository cartDetailRepository,
            CartRepository cartRepository
    ) {
        this.orderDetailService = orderDetailService;
        this.orderService = orderService;
        this.productService = productService;
        this.cartRepository = cartRepository;
        this.cartDetailRepository = cartDetailRepository;
    }

    @Override
    public PaymentResponse createPayment(HttpServletRequest request, Long orderId) {
        Order order = orderService.findOrderById(orderId);
        if (order.getStatus().equals(StatusType.COMPLETED)) return new PaymentResponse("Ok", "Successfully", null);
        if (order.getStatus().equals(StatusType.CANCELLED))
            return new PaymentResponse("Cancelled", "This payment was cancelled", null);
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
            VnPayQueryAndSecureHash vnPayQueryAndSecureHash = VnPayConfig.hashAllFields(vnpParams);
            String paymentUrl = VnPayConfig.VNP_PAY_URL + "?" +
                    vnPayQueryAndSecureHash.query() +
                    "&vnp_SecureHash=" +
                    vnPayQueryAndSecureHash.secureHash();
            return new PaymentResponse("Ok", "Processing Payment", paymentUrl);
        }
        throw new BadRequestException(Error.Message.PAYMENT_FAILED);
    }

    @Override
    public PaymentResponse getPayment(PaymentGetVm paymentGetVm) {
        Map<String, String> vnpParams = mappingPaymentInformation(paymentGetVm);
        VnPayQueryAndSecureHash vnPayQueryAndSecureHash = VnPayConfig.hashAllFields(vnpParams);
        String secureHash = vnPayQueryAndSecureHash.secureHash();
        if (paymentGetVm.secureHash().equalsIgnoreCase(secureHash)) {
            if (paymentGetVm.transactionStatus().equalsIgnoreCase("00"))
                return generatePayment(StatusType.COMPLETED, paymentGetVm.orderId(), Message.PAYMENT_COMPLETED);
            if (paymentGetVm.transactionStatus().equalsIgnoreCase("01"))
                return generatePayment(StatusType.PENDING, paymentGetVm.orderId(), Message.PAYMENT_PENDING);
            if (paymentGetVm.transactionStatus().equalsIgnoreCase("02"))
                return generatePayment(StatusType.CANCELLED, paymentGetVm.orderId(), Message.PAYMENT_CANCELLED);
        }
        throw new BadRequestException(Error.Message.PAYMENT_FAILED);
    }

    private static Map<String, String> mappingPaymentInformation(PaymentGetVm paymentGetVm) {
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

    private PaymentResponse generatePayment(StatusType status, Long orderId, String message) {
        Order order = orderService.findOrderById(orderId);
        if (order.getStatus().equals(StatusType.COMPLETED))
            return new PaymentResponse(status.toString(), message, null);
        if (order.getStatus().equals(StatusType.PENDING) && status.equals(StatusType.COMPLETED)) {
            order.setStatus(StatusType.COMPLETED);
            Cart cart = cartRepository.findCartByUser_Email(order.getCreatedBy()).orElseThrow(() -> new NotFoundException("Not found Cart"));
            orderDetailService.findByOrderId(orderId).forEach(orderDetail -> {
                Product product = orderDetail.getProduct();
                product.setStockQuantity(product.getStockQuantity() - orderDetail.getQuantity());
                productService.saveProduct(product);
                CartDetail cartDetail = cartDetailRepository.findByCart_IdAndProduct_Id(cart.getId(), product.getId()).orElseThrow(() -> new NotFoundException("Not found CartDetail"));
                cartDetailRepository.deleteById(cartDetail.getId());
            });
            orderService.save(order);
            return new PaymentResponse(status.toString(), message, null);
        }
        order.setStatus(status);
        orderService.save(order);
        return new PaymentResponse(status.toString(), message, null);
    }
}
