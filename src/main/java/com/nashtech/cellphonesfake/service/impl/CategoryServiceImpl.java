package com.nashtech.cellphonesfake.service.impl;

import com.nashtech.cellphonesfake.constant.Error;
import com.nashtech.cellphonesfake.exception.NotFoundException;
import com.nashtech.cellphonesfake.mapper.CategoryMapper;
import com.nashtech.cellphonesfake.model.Category;
import com.nashtech.cellphonesfake.repository.CategoryRepository;
import com.nashtech.cellphonesfake.service.CategoryService;
import com.nashtech.cellphonesfake.view.CategoryAdminVm;
import com.nashtech.cellphonesfake.view.CategoryVm;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(final CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<CategoryVm> findAllCategories() {
        return categoryRepository.findAllByIsPublishedIsTrue().stream().map(CategoryMapper.INSTANCE::toCategoryVm).toList();
    }

    @Override
    public List<CategoryAdminVm> findAllCategoriesBackOffice() {
        return categoryRepository.findAll().stream().map(CategoryMapper.INSTANCE::toCategoryAdminVm).toList();
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
        Category savedCategory = categoryRepository.save(CategoryMapper.INSTANCE.toCategory(newCategoryVm));
        return CategoryMapper.INSTANCE.toCategoryVm(savedCategory);
    }

    @Override
    public Category findCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format(Error.Message.RESOURCE_NOT_FOUND_BY_ID, "Category", id))
        );
    }

    @Override
    public CategoryAdminVm findCategoryAdminVmById(Long id) {
        Category category = findCategoryById(id);
        return CategoryMapper.INSTANCE.toCategoryAdminVm(category);
    }

}
