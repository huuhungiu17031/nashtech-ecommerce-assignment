package com.nashtech.cellphonesfake.service.impl;

import com.nashtech.cellphonesfake.mapper.ProductGalleryMapper;
import com.nashtech.cellphonesfake.repository.ProductGalleryRepository;
import com.nashtech.cellphonesfake.service.ProductGalleryService;
import com.nashtech.cellphonesfake.view.ProductGalleryVm;
import jakarta.transaction.Transactional;
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

    @Override
    public ProductGalleryVm getListGalleryInCart(Long productId) {
        return productGalleryRepository.findByProductIdAndThumbnailTrue(productId).map(ProductGalleryMapper.INSTANCE::toProductGalleryVm).orElse(null);
    }

    @Transactional
    @Override
    public String deleteGallery(String imageName) {
        productGalleryRepository.deleteByImagePath(imageName);
        return "Delete successfully";
    }
}
