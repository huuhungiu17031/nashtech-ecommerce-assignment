package com.nashtech.cellphonesfake.mapper;

import com.nashtech.cellphonesfake.model.ProductGallery;
import com.nashtech.cellphonesfake.view.ProductGalleryVm;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductGalleryMapper {
    ProductGalleryMapper INSTANCE = Mappers.getMapper(ProductGalleryMapper.class);
    ProductGalleryVm toProductGalleryVm(ProductGallery productGallery);
}
