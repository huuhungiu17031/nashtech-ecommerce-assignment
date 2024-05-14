package com.nashtech.cellphonesfake.service;


import com.nashtech.cellphonesfake.model.Brand;
import com.nashtech.cellphonesfake.view.BrandPostVm;
import com.nashtech.cellphonesfake.view.BrandVm;

import java.util.List;

public interface BrandService {
    List<Brand> createNewBrands(List<BrandVm> newBrandVmList);
    List<BrandVm> findAllBrands();
    Brand updateBrand(Long id, BrandPostVm brandPostVm);
    List<BrandVm> findAllBrandsByCategoryId(Long categoryId);
}
