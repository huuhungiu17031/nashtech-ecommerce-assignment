package com.nashtech.cellphonesfake.service.impl;

import com.nashtech.cellphonesfake.constant.Error;
import com.nashtech.cellphonesfake.constant.Message;
import com.nashtech.cellphonesfake.exception.NotFoundException;
import com.nashtech.cellphonesfake.model.Cart;
import com.nashtech.cellphonesfake.model.CartDetail;
import com.nashtech.cellphonesfake.model.User;
import com.nashtech.cellphonesfake.repository.CartRepository;
import com.nashtech.cellphonesfake.service.CartDetailService;
import com.nashtech.cellphonesfake.service.CartService;
import com.nashtech.cellphonesfake.view.CartDetailRequest;
import com.nashtech.cellphonesfake.view.CartVm;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final CartDetailService cartDetailService;

    public CartServiceImpl(CartRepository cartRepository, CartDetailService cartDetailService) {
        this.cartRepository = cartRepository;
        this.cartDetailService = cartDetailService;
    }

    @Override
    public void createCart(User user) {
        Cart cart = new Cart();
        cart.setUser(user);
        cartRepository.save(cart);
    }

    @Transactional
    @Override
    public String addToCart(CartDetailRequest cartDetailRequest) {
        String principal = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Cart cart = findCartByUserEmail(principal);
        CartDetail cartDetail = cartDetailService.findByCartIdAndProductId(cart.getId(), cartDetailRequest.productId(), cartDetailRequest.amount(), cart);
        if (cartDetail.getId() != null) {
            cartDetail.setAmount(cartDetail.getAmount() + cartDetailRequest.amount());
        }
        cartDetailService.saveCartDetail(cartDetail);
        return Message.CART_ADD_SUCCESS;
    }

    @Override
    public CartVm getCartByUserEmail(String userEmail) {
        Cart cart = findCartByUserEmail(userEmail);
        return new CartVm(cart.getId());
    }

    private Cart findCartByUserEmail(String email) {
        return cartRepository.findCartByUser_Email(email)
                .orElseThrow(() -> new NotFoundException(String.format(Error.Message.RESOURCE_NOT_FOUND_BY_ID, "Email", email)));
    }

}
