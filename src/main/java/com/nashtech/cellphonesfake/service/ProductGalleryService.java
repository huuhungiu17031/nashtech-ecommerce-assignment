package com.nashtech.cellphonesfake.service;

import com.nashtech.cellphonesfake.view.ProductGalleryVm;

import java.util.List;

public interface ProductGalleryService {
    List<ProductGalleryVm> getListGalleryByProductId(Long productId);
}
