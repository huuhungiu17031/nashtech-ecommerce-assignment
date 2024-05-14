package com.nashtech.cellphonesfake.service.impl;

import com.nashtech.cellphonesfake.constant.Error;
import com.nashtech.cellphonesfake.exception.NotFoundException;
import com.nashtech.cellphonesfake.mapper.CategoryMapper;
import com.nashtech.cellphonesfake.model.Category;
import com.nashtech.cellphonesfake.repository.CategoryRepository;
import com.nashtech.cellphonesfake.service.CategoryService;
import com.nashtech.cellphonesfake.view.CategoryVm;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {

    CategoryRepository categoryRepository;

    @Override
    public List<CategoryVm> findAllCategories() {
        return categoryRepository.findAll().stream().map(CategoryMapper.INSTANCE::toCategoryVm).toList();
    }

    @Override
    public List<CategoryVm> createCategoryVms(List<CategoryVm> categoryVmList) {
        return categoryVmList.stream().map(categoryVm -> {
                    Category newCategory = categoryRepository
                            .save(CategoryMapper.INSTANCE.toCategory(categoryVm));
                    return CategoryMapper.INSTANCE.toCategoryVm(newCategory);
                }
        ).toList();
    }

    @Override
    public CategoryVm updateCategory(CategoryVm newCategoryVm) {
        return categoryRepository.findById(newCategoryVm.id()).map(CategoryMapper.INSTANCE::toCategoryVm).orElseThrow(
                () -> new NotFoundException(String.format(Error.Message.RESOURCE_NOT_FOUND_BY_ID, "Category", newCategoryVm.id()))
        );
    }

}
