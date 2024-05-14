package com.nashtech.cellphonesfake.service.impl;

import com.nashtech.cellphonesfake.constant.Error;
import com.nashtech.cellphonesfake.exception.NotFoundException;
import com.nashtech.cellphonesfake.mapper.BrandMapper;
import com.nashtech.cellphonesfake.model.Brand;
import com.nashtech.cellphonesfake.repository.BrandRepository;
import com.nashtech.cellphonesfake.service.BrandService;
import com.nashtech.cellphonesfake.view.BrandPostVm;
import com.nashtech.cellphonesfake.view.BrandVm;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class BrandServiceImpl implements BrandService {
    BrandRepository brandRepository;

    @Override
    public List<Brand> createNewBrands(List<BrandVm> newBrandVmList) {
        return newBrandVmList.stream().map(brandVm -> {
            return brandRepository.save(
                    BrandMapper.INSTANCE.toBrand(brandVm)
            );
        }).toList();
    }

    @Override
    public List<BrandVm> findAllBrands() {
        return brandRepository
                .findAll()
                .stream()
                .map(BrandMapper.INSTANCE::toBrandVm).toList();
    }

    @Override
    public Brand updateBrand(Long id, BrandPostVm brandPostVm) {
        brandRepository.findById(id).orElseThrow(() -> new NotFoundException(String.format(Error.Message.RESOURCE_NOT_FOUND_BY_ID, "Brand", id)));
        Brand newBrand = BrandMapper.INSTANCE.fromBrandPostVmToBrand(brandPostVm);
        newBrand.setId(id);
        return brandRepository.save(newBrand);
    }

    @Override
    public List<BrandVm> findAllBrandsByCategoryId(Long categoryId) {
        return brandRepository.findDistinctByCategoriesId(categoryId).stream().map(BrandMapper.INSTANCE::toBrandVm).toList();
    }
}
