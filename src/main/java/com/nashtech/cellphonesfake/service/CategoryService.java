package com.nashtech.cellphonesfake.service;

import com.nashtech.cellphonesfake.model.Category;
import com.nashtech.cellphonesfake.view.CategoryVm;

import java.util.List;

public interface CategoryService {
    List<CategoryVm> findAllCategories();

    List<CategoryVm> createCategoryVms(List<CategoryVm> categoryVmList);

    CategoryVm updateCategory(CategoryVm newCategoryVm);

    Category findCategoryById(Long id);
}
