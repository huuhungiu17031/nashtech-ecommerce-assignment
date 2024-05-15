package com.nashtech.cellphonesfake.controller;

import com.nashtech.cellphonesfake.model.Brand;
import com.nashtech.cellphonesfake.service.BrandService;
import com.nashtech.cellphonesfake.view.BrandPostVm;
import com.nashtech.cellphonesfake.view.BrandVm;
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
@RequestMapping("/brand")
public class BrandController {
    private final BrandService brandService;

    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

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
