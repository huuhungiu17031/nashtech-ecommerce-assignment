package com.nashtech.cellphonesfake.service;

import com.nashtech.cellphonesfake.model.Cart;
import com.nashtech.cellphonesfake.model.CartDetail;
import com.nashtech.cellphonesfake.view.CartDetailGetVm;

import java.util.List;

public interface CartDetailService {
    String deleteCartDetail(Long cartId);
    String updateCartDetail(Long cartId, Long amount);
    String deleteAllCartDetails(Long cartId);
    List<CartDetailGetVm> findByCartId(Long cartId);
    CartDetail findByCartIdAndProductId(Long cartId, Long productId, Long amount, Cart cart);
    CartDetail saveCartDetail(CartDetail cartDetail);
    CartDetail findCartDetailById(Long cartId);
}
