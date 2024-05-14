package com.nashtech.cellphonesfake.controller;

import com.nashtech.cellphonesfake.service.CategoryService;
import com.nashtech.cellphonesfake.view.CategoryVm;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryController {
    CategoryService categoryService;

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
