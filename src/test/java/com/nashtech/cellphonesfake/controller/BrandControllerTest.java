package com.nashtech.cellphonesfake.controller;

import com.nashtech.cellphonesfake.model.Brand;
import com.nashtech.cellphonesfake.service.BrandService;
import com.nashtech.cellphonesfake.view.BrandPostVm;
import com.nashtech.cellphonesfake.view.BrandVm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BrandControllerTest {
    @Mock
    private BrandService brandService;

    @InjectMocks
    private BrandController brandController;
    private  BrandVm initBrandVm2;
    private List<BrandVm> newBrandVmList;
    private BrandVm initBrandVm;
    @BeforeEach
    public void setUp() {
        initBrandVm = new BrandVm(1L, "Test Brand", "image");
        initBrandVm2 = new BrandVm(2L, "Test Brand", "image");

        newBrandVmList = Collections.singletonList(initBrandVm);
    }

    @Test
    void createNewBrands_success() {
        when(brandService.createNewBrands(anyList())).thenReturn(Collections.singletonList(new Brand()));

        ResponseEntity<List<Brand>> responseEntity = brandController.createNewBrands(newBrandVmList);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(1, Objects.requireNonNull(responseEntity.getBody()).size());
    }

    @Test
    void testGetAllBrands_whenBrandsExist_returnsOkResponseWithBrands() {
        List<BrandVm> expectedBrandVms = Arrays.asList(initBrandVm, initBrandVm2);
        when(brandService.findAllBrands()).thenReturn(expectedBrandVms);
        ResponseEntity<List<BrandVm>> responseEntity = brandController.getAllBrands();
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedBrandVms, responseEntity.getBody());
    }

    @Test
    void testGetAllBrandsByCategoryId_whenBrandsExist_shouldReturnOkResponseWithBrands() {
        // Given
        Long categoryId = 1L;
        List<BrandVm> expectedBrands = Arrays.asList(initBrandVm, initBrandVm2);
        when(brandService.findAllBrandsByCategoryId(categoryId)).thenReturn(expectedBrands);

        // When
        ResponseEntity<List<BrandVm>> responseEntity = brandController.getAllBrandsAsd(categoryId);

        // Then
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedBrands, responseEntity.getBody());
    }

    @Test
    void testUpdateBrand_success() {
        // Given
        Long brandId = 1L;
        BrandPostVm brandPostVm = new BrandPostVm("Test Brand","image");
        Brand updatedBrand = new Brand();
        updatedBrand.setId(brandId);
        updatedBrand.setName("Test Brand");
        updatedBrand.setImagePath("image");
        when(brandService.updateBrand(eq(brandId), any(BrandPostVm.class))).thenReturn(updatedBrand);

        ResponseEntity<Brand> responseEntity = brandController.updateBrand(brandId, brandPostVm);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(updatedBrand, responseEntity.getBody());
    }
}
