package com.nashtech.cellphonesfake.service;

import com.nashtech.cellphonesfake.model.Cart;
import com.nashtech.cellphonesfake.model.CartDetail;
import com.nashtech.cellphonesfake.model.User;
import com.nashtech.cellphonesfake.view.CartDetailRequest;

public interface CartService {
    void createCart(User user);

    String addToCart(CartDetailRequest item);

    String removeToCart(CartDetail item, Long userId);

    void save(Cart cart);

    Cart getCart(Long userId);
}
