package com.nashtech.cellphonesfake.mapper;

import com.nashtech.cellphonesfake.model.Category;
import com.nashtech.cellphonesfake.view.CategoryAdminVm;
import com.nashtech.cellphonesfake.view.CategoryVm;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CategoryMapper {
    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    CategoryVm toCategoryVm(Category category);

    Category toCategory(CategoryVm categoryVm);

    CategoryAdminVm toCategoryAdminVm(Category category);
}
