package com.nashtech.cellphonesfake.controller;

import com.nashtech.cellphonesfake.service.CategoryService;
import com.nashtech.cellphonesfake.view.CategoryVm;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<CategoryVm>> getAllCategories() {
        return new ResponseEntity<>(categoryService.findAllCategories(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<List<CategoryVm>> createCategory(@Valid @RequestBody List<CategoryVm> categoryVmList) {
        return new ResponseEntity<>(categoryService.createCategoryVms(categoryVmList), HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<CategoryVm> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryVm categoryVm) {
        return new ResponseEntity<>(categoryService.updateCategory(categoryVm), HttpStatus.OK);
    }

}
