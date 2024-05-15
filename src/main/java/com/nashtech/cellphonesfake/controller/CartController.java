package com.nashtech.cellphonesfake.controller;

import com.nashtech.cellphonesfake.service.CartService;
import com.nashtech.cellphonesfake.view.CartDetailRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @Transactional
    @PostMapping
    public ResponseEntity<String> addToCart(@Valid @RequestBody CartDetailRequest cartDetailRequest) {
        return new ResponseEntity<>(cartService.addToCart(cartDetailRequest), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Void> getCartBelongToUser(@PathVariable Long id) {
        return ResponseEntity.ok().build();
    }

}
