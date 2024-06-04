package com.nashtech.cellphonesfake.service;

import com.nashtech.cellphonesfake.model.Category;
import com.nashtech.cellphonesfake.view.CategoryAdminVm;
import com.nashtech.cellphonesfake.view.CategoryVm;
import com.nashtech.cellphonesfake.view.PaginationVm;

import java.util.List;

public interface CategoryService {
    List<CategoryVm> findAllCategories();
    PaginationVm findAllCategoriesBackOffice(int page, int size);

    List<CategoryVm> createCategoryVms(List<CategoryVm> categoryVmList);

    CategoryVm updateCategory(CategoryVm newCategoryVm);

    Category findCategoryById(Long id);
    CategoryAdminVm findCategoryAdminVmById(Long id);
}
