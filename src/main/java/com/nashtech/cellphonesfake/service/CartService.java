package com.nashtech.cellphonesfake.service;

import com.nashtech.cellphonesfake.model.User;
import com.nashtech.cellphonesfake.view.CartDetailRequest;

public interface CartService {
    void createCart(User user);

    String addToCart(CartDetailRequest item);
}
