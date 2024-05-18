package com.nashtech.cellphonesfake.service.impl;

import com.nashtech.cellphonesfake.mapper.ProductGalleryMapper;
import com.nashtech.cellphonesfake.repository.ProductGalleryRepository;
import com.nashtech.cellphonesfake.service.ProductGalleryService;
import com.nashtech.cellphonesfake.view.ProductGalleryVm;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductGalleryServiceImpl implements ProductGalleryService {
    private final ProductGalleryRepository productGalleryRepository;

    public ProductGalleryServiceImpl(ProductGalleryRepository productGalleryRepository) {
        this.productGalleryRepository = productGalleryRepository;
    }

    @Override
    public List<ProductGalleryVm> getListGalleryByProductId(Long productId) {
        return productGalleryRepository.findByProduct_Id(productId).stream().map(ProductGalleryMapper.INSTANCE::toProductGalleryVm).toList();
    }
}
