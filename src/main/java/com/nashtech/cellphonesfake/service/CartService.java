package com.nashtech.cellphonesfake.service;

import com.nashtech.cellphonesfake.model.Cart;
import com.nashtech.cellphonesfake.model.CartDetail;

public interface CartService {
    String createCart(Cart cart);
    String addToCart(CartDetail item, Long userId);
    String removeToCart(CartDetail item, Long userId);
    void save(Cart cart);
}
