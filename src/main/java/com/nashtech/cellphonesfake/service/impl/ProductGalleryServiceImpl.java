package com.nashtech.cellphonesfake.service.impl;

import com.nashtech.cellphonesfake.mapper.ProductGalleryMapper;
import com.nashtech.cellphonesfake.repository.ProductGalleryRepository;
import com.nashtech.cellphonesfake.service.ProductGalleryService;
import com.nashtech.cellphonesfake.view.ProductGalleryVm;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ProductGalleryServiceImpl implements ProductGalleryService {
    ProductGalleryRepository productGalleryRepository;

    @Override
    public List<ProductGalleryVm> getListGalleryByProductId(Long productId) {
        return productGalleryRepository.findByProduct_Id(productId).stream().map(ProductGalleryMapper.INSTANCE::toProductGalleryVm).toList();
    }
}
