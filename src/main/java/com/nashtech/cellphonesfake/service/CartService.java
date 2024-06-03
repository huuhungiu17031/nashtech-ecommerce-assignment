package com.nashtech.cellphonesfake.service;

import com.nashtech.cellphonesfake.model.User;
import com.nashtech.cellphonesfake.view.CartDetailRequest;
import com.nashtech.cellphonesfake.view.CartVm;

public interface CartService {
    void createCart(User user);

    String addToCart(CartDetailRequest item);

    CartVm getCartByUserEmail(String email);
}
