package com.nashtech.cellphonesfake.mapper;

import com.nashtech.cellphonesfake.model.Brand;
import com.nashtech.cellphonesfake.view.BrandPostVm;
import com.nashtech.cellphonesfake.view.BrandVm;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BrandMapper {
    BrandMapper INSTANCE = Mappers.getMapper(BrandMapper.class);

    BrandVm toBrandVm(Brand brand);

    Brand toBrand(BrandVm brandVm);

    Brand fromBrandPostVmToBrand(BrandPostVm brandPostVm);
}
