package com.nashtech.cellphonesfake.service;

import com.nashtech.cellphonesfake.view.PaginationVm;
import com.nashtech.cellphonesfake.view.ProductDetailVm;
import com.nashtech.cellphonesfake.view.ProductPostVm;

public interface ProductService {
    ProductDetailVm getProductDetail(Long id);

    PaginationVm getProductCardVmByCategory(Long categoryId, Long brandId, String field, String dir, int page, int size);

    void createProduct(ProductPostVm productPostVm);
}
