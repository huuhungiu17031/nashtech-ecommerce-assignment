package com.nashtech.cellphonesfake.service;

import com.nashtech.cellphonesfake.view.ProductGalleryVm;

import java.util.List;
import java.util.Optional;

public interface ProductGalleryService {
    List<ProductGalleryVm> getListGalleryByProductId(Long productId);
    ProductGalleryVm getListGalleryInCart(Long productId);
    String deleteGallery(String imageName);
}
