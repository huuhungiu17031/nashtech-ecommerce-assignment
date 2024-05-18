package com.nashtech.cellphonesfake.controller;

import com.nashtech.cellphonesfake.service.ProductGalleryService;
import com.nashtech.cellphonesfake.view.ProductGalleryVm;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/product-gallery")
public class ProductGalleryController {
    private final ProductGalleryService productGalleryService;

    public ProductGalleryController(ProductGalleryService productGalleryService) {
        this.productGalleryService = productGalleryService;
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Collection<ProductGalleryVm>> getProductGalleryByProductId(@PathVariable Long productId) {
        return new ResponseEntity<>(productGalleryService.getListGalleryByProductId(productId), HttpStatus.OK);
    }
}
