package com.nashtech.cellphonesfake.service;

import com.nashtech.cellphonesfake.exception.NotFoundException;
import com.nashtech.cellphonesfake.model.Brand;
import com.nashtech.cellphonesfake.repository.BrandRepository;
import com.nashtech.cellphonesfake.service.impl.BrandServiceImpl;
import com.nashtech.cellphonesfake.view.BrandPostVm;
import com.nashtech.cellphonesfake.view.BrandVm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class BrandServiceTest {
    @Mock
    private BrandRepository brandRepository;

    @InjectMocks
    private BrandServiceImpl brandServiceImpl;

    private Brand initBrand;
    private BrandVm brandVm1;
    private BrandVm brandVm2;
    private Brand initBrand2;

    @BeforeEach
    public void init() {
        initBrand = Brand.builder().id(1L).name("test").imagePath("images").categories(null).products(null).build();
        brandVm1 = new BrandVm(1L,"Brand 1", "images");
        brandVm2 = new BrandVm(2L,"Brand 2", "images");
        initBrand2 = Brand.builder().id(2L).name("test").imagePath("images").categories(null).products(null).build();
    }

    @Test
    void BrandService_findAllBrands_ReturnListBrandVm() {
        when(brandRepository.findAll()).thenReturn(Collections.singletonList(initBrand));
        List<BrandVm> brandVms = brandServiceImpl.findAllBrands();
        assertThat(brandVms).isNotNull();
    }

    @Test
    void BrandService_findBrandById_ReturnBrand() {
        when(brandRepository.findById(1L)).thenReturn(Optional.of(initBrand));
        Brand foundBrand = brandServiceImpl.findBrandById(1L);
        assertThat(foundBrand).isNotNull();
        assertThat(foundBrand.getId()).isEqualTo(initBrand.getId());
        assertThat(foundBrand.getName()).isEqualTo(initBrand.getName());
        assertThat(foundBrand.getImagePath()).isEqualTo(initBrand.getImagePath());
    }

    @Test
    void BrandService_testFindBrandById_NotFoundException() {
        Long id = 1L;
        when(brandRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> brandServiceImpl.findBrandById(id));
    }

    @Test
    void BrandService_findAllBrandsByCategoryId_ReturnListBrandVm() {
        when(brandRepository.findDistinctByCategoriesId(1L)).thenReturn(Collections.singletonList(initBrand));
        List<BrandVm> brandVms = brandServiceImpl.findAllBrandsByCategoryId(1L);
        assertThat(brandVms).isNotNull();
    }

    @Test
    void BrandService_createNewBrands_ReturnListBrand() {
        List<BrandVm> newBrandVmList = Arrays.asList(brandVm1, brandVm2);
        when(brandRepository.save(any(Brand.class))).thenReturn(initBrand, initBrand2);
        List<Brand> result = brandServiceImpl.createNewBrands(newBrandVmList);
        assertEquals(2, result.size());
        assertEquals(initBrand, result.get(0));
        assertEquals(initBrand2, result.get(1));
    }

    @Test
    void BrandService_updateBrand_whenBrandDoesNotExist() {
        // Given
        Long brandId = 1L;
        BrandPostVm brandPostVm = new BrandPostVm("test","Updated Brand");
        when(brandRepository.findById(brandId)).thenReturn(Optional.empty());
        NotFoundException notFoundException = assertThrows(NotFoundException.class, () -> brandServiceImpl.updateBrand(brandId, brandPostVm));
        assertEquals("Brand not found with the given: '1'", notFoundException.getMessage());
    }

}
