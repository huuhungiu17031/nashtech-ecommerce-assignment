package com.nashtech.cellphonesfake.controller;

import com.nashtech.cellphonesfake.service.CartDetailService;
import com.nashtech.cellphonesfake.view.CartDetailGetVm;
import com.nashtech.cellphonesfake.view.CartDetailVm;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cart-detail")
public class CartDetailController {
    private final CartDetailService cartDetailService;

    public CartDetailController(CartDetailService cartDetailService) {
        this.cartDetailService = cartDetailService;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCartDetail(@PathVariable Long id) {
        return new ResponseEntity<>(cartDetailService.deleteCartDetail(id), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<String> updateCartDetail(@RequestBody @Valid CartDetailVm cartDetailVm) {
        return new ResponseEntity<>(cartDetailService.updateCartDetail(cartDetailVm.id(), cartDetailVm.amount()), HttpStatus.OK);
    }

    @DeleteMapping("/{cartId}/all")
    public ResponseEntity<String> deleteAllCartDetails(@PathVariable Long cartId) {
        return new ResponseEntity<>(cartDetailService.deleteAllCartDetails(cartId), HttpStatus.OK);
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<List<CartDetailGetVm>> getAllCartDetails(@PathVariable Long cartId) {
        return new ResponseEntity<>(cartDetailService.findByCartId(cartId), HttpStatus.OK);
    }
}
