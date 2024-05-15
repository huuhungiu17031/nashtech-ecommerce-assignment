package com.nashtech.cellphonesfake.service.impl;

import com.nashtech.cellphonesfake.constant.Error;
import com.nashtech.cellphonesfake.exception.NotFoundException;
import com.nashtech.cellphonesfake.model.Cart;
import com.nashtech.cellphonesfake.model.CartDetail;
import com.nashtech.cellphonesfake.model.User;
import com.nashtech.cellphonesfake.repository.CartRepository;
import com.nashtech.cellphonesfake.service.CartService;
import com.nashtech.cellphonesfake.view.CartDetailRequest;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;

    public CartServiceImpl(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    @Override
    public void createCart(User user) {
        Cart cart = new Cart();
        cart.setUser(user);
        cartRepository.save(cart);
    }

    @Override
    public String addToCart(CartDetailRequest cartDetail) {
        String principal = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Cart cart = findCartByEmail(principal);

        return "";
    }

    @Override
    public String removeToCart(CartDetail item, Long userId) {
        return "";
    }

    @Override
    public void save(Cart cart) {

    }

    @Override
    public Cart getCart(Long userId) {
        return cartRepository.findCartByUser_Id(userId).orElseThrow(() -> new NotFoundException(Error.Message.RESOURCE_NOT_FOUND_BY_ID, "User", userId));
    }

    private Cart findCartByEmail(String email) {
        return cartRepository.findCartByUser_Email(email).orElseThrow(() -> new NotFoundException(Error.Message.RESOURCE_NOT_FOUND_BY_ID, "Email", email));
    }
}
