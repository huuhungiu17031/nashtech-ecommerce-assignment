package com.nashtech.cellphonesfake.service.impl;

import com.nashtech.cellphonesfake.constant.Error;
import com.nashtech.cellphonesfake.exception.NotFoundException;
import com.nashtech.cellphonesfake.mapper.BrandMapper;
import com.nashtech.cellphonesfake.model.Brand;
import com.nashtech.cellphonesfake.repository.BrandRepository;
import com.nashtech.cellphonesfake.service.BrandService;
import com.nashtech.cellphonesfake.view.BrandPostVm;
import com.nashtech.cellphonesfake.view.BrandVm;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandServiceImpl implements BrandService {
    private final BrandRepository brandRepository;

    public BrandServiceImpl(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    @Override
    public List<Brand> createNewBrands(List<BrandVm> newBrandVmList) {
        return newBrandVmList.stream().map(brandVm -> brandRepository.save(
                        BrandMapper.INSTANCE.toBrand(brandVm)
                )
        ).toList();
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
        if (brandRepository.findById(id).isEmpty())
            throw new NotFoundException(String.format(Error.Message.RESOURCE_NOT_FOUND_BY_ID, "Brand", id));
        Brand newBrand = BrandMapper.INSTANCE.fromBrandPostVmToBrand(brandPostVm);
        newBrand.setId(id);
        return brandRepository.save(newBrand);
    }

    @Override
    public List<BrandVm> findAllBrandsByCategoryId(Long categoryId) {
        return brandRepository.findDistinctByCategoriesId(categoryId).stream().map(BrandMapper.INSTANCE::toBrandVm).toList();
    }
}
