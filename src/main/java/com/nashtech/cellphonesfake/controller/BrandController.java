package com.nashtech.cellphonesfake.controller;

import com.nashtech.cellphonesfake.model.Brand;
import com.nashtech.cellphonesfake.service.BrandService;
import com.nashtech.cellphonesfake.view.BrandPostVm;
import com.nashtech.cellphonesfake.view.BrandVm;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/brand")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BrandController {
    BrandService brandService;

    @PostMapping
    public ResponseEntity<List<Brand>> createNewBrands(@RequestBody List<BrandVm> newBrandVmList) {
        return new ResponseEntity<>(brandService.createNewBrands(newBrandVmList), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<BrandVm>> getAllBrands() {
        return new ResponseEntity<>(brandService.findAllBrands(), HttpStatus.OK);
    }

    @GetMapping("/category-id/{categoryId}")
    public ResponseEntity<List<BrandVm>> getAllBrandsAsd(@PathVariable Long categoryId) {
        return new ResponseEntity<>(brandService.findAllBrandsByCategoryId(categoryId), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Brand> updateBrand(
            @PathVariable Long id,
            @Valid @RequestBody BrandPostVm brandUpdateVm
    ) {
        return new ResponseEntity<>(brandService.updateBrand(id, brandUpdateVm), HttpStatus.OK);
    }
}
