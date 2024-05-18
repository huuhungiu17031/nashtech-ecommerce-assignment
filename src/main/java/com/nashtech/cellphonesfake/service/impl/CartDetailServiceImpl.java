package com.nashtech.cellphonesfake.service.impl;

import com.nashtech.cellphonesfake.constant.Error;
import com.nashtech.cellphonesfake.exception.NotFoundException;
import com.nashtech.cellphonesfake.model.Cart;
import com.nashtech.cellphonesfake.model.CartDetail;
import com.nashtech.cellphonesfake.model.Product;
import com.nashtech.cellphonesfake.repository.CartDetailRepository;
import com.nashtech.cellphonesfake.service.CartDetailService;
import com.nashtech.cellphonesfake.service.ProductService;
import com.nashtech.cellphonesfake.view.CartDetailGetVm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CartDetailServiceImpl implements CartDetailService {
    private final CartDetailRepository cartDetailRepository;
    private final ProductService productService;

    public CartDetailServiceImpl(CartDetailRepository cartDetailRepository, ProductService productService) {
        this.cartDetailRepository = cartDetailRepository;
        this.productService = productService;
    }

    @Override
    public String deleteCartDetail(Long cartDetailId) {
        CartDetail cartDetail = findCartDetailById(cartDetailId);
        cartDetailRepository.delete(cartDetail);
        return "Deleted successfully from cart";
    }

    @Override
    public String updateCartDetail(Long cartDetailId, Long amount) {
        CartDetail cartDetail = findCartDetailById(cartDetailId);
        if (amount == 0) {
            cartDetailRepository.deleteById(cartDetailId);
        } else {
            cartDetail.setAmount(amount);
            cartDetailRepository.save(cartDetail);
        }
        return "Updated successfully from cart";
    }

    @Override
    public String deleteAllCartDetails(Long cartId) {
        List<CartDetail> cartDetails = cartDetailRepository.findByCart_Id(cartId);
        cartDetailRepository.deleteAll(cartDetails);
        return "Deleted successfully from cart";
    }

    @Override
    public List<CartDetailGetVm> findByCartId(Long cartId) {
        return cartDetailRepository.findByCart_Id(cartId).stream().map(cartDetail ->
                new CartDetailGetVm(cartDetail.getId(), cartDetail.getProduct().getId(), cartDetail.getAmount())).toList();
    }

    @Override
    public CartDetail findByCartIdAndProductId(Long cartId, Long productId, Long amount, Cart cart) {
        return cartDetailRepository.findByCart_IdAndProduct_Id(cartId, productId)
                .orElseGet(() -> {
                    CartDetail newCartDetail = new CartDetail();
                    newCartDetail.setCart(cart);
                    Product product = productService.findProductById(productId);
                    newCartDetail.setProduct(product);
                    newCartDetail.setAmount(amount);
                    return newCartDetail;
                });
    }

    @Override
    public CartDetail saveCartDetail(CartDetail cartDetail) {
        return cartDetailRepository.save(cartDetail);
    }

    @Override
    public CartDetail findCartDetailById(Long cartDetailId) {
        return cartDetailRepository.findById(cartDetailId).orElseThrow(
                () -> new NotFoundException(String.format(Error.Message.RESOURCE_NOT_FOUND_BY_ID, "Cart detail", cartDetailId))
        );
    }
}
