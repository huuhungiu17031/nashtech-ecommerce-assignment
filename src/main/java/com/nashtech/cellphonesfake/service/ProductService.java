package com.nashtech.cellphonesfake.service;

import com.nashtech.cellphonesfake.enumeration.PaymentMethod;
import com.nashtech.cellphonesfake.model.Product;
import com.nashtech.cellphonesfake.view.PaginationVm;
import com.nashtech.cellphonesfake.view.ProductDetailVm;
import com.nashtech.cellphonesfake.view.ProductPostVm;

public interface ProductService {
    PaginationVm getProductCardVmByCategory(Long categoryId, Long brandId, String field, String dir, int page, int size);

    ProductDetailVm getProductDetail(Long id);

    void createProduct(ProductPostVm productPostVm);

    Product findProductById(Long id);

    Product checkProductAmountAndReduceStockQuantity(Long productId, Long amount, PaymentMethod paymentMethod);

    PaginationVm getProductCardAdminVmByCategory(Long brandId, String field, String dir, int page, int size);

    void saveProduct(Product product);
}
