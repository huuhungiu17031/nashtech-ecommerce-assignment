package com.nashtech.cellphonesfake.service.impl;

import com.nashtech.cellphonesfake.constant.Error;
import com.nashtech.cellphonesfake.exception.NotFoundException;
import com.nashtech.cellphonesfake.mapper.ProductMapper;
import com.nashtech.cellphonesfake.model.Brand;
import com.nashtech.cellphonesfake.model.Category;
import com.nashtech.cellphonesfake.model.Product;
import com.nashtech.cellphonesfake.model.ProductGallery;
import com.nashtech.cellphonesfake.repository.BrandRepository;
import com.nashtech.cellphonesfake.repository.CategoryRepository;
import com.nashtech.cellphonesfake.repository.ProductGalleryRepository;
import com.nashtech.cellphonesfake.repository.ProductRepository;
import com.nashtech.cellphonesfake.service.ProductService;
import com.nashtech.cellphonesfake.view.PaginationVm;
import com.nashtech.cellphonesfake.view.ProductCardVm;
import com.nashtech.cellphonesfake.view.ProductDetailVm;
import com.nashtech.cellphonesfake.view.ProductPostVm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductGalleryRepository productGalleryRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;

    public ProductServiceImpl(ProductRepository productRepository, ProductGalleryRepository productGalleryRepository, CategoryRepository categoryRepository, BrandRepository brandRepository) {
        this.productRepository = productRepository;
        this.productGalleryRepository = productGalleryRepository;
        this.categoryRepository = categoryRepository;
        this.brandRepository = brandRepository;
    }

    @Override
    public ProductDetailVm getProductDetail(Long id) {
        return productRepository.findById(id).map(ProductMapper.INSTANCE::toProductDetailVm)
                .orElseThrow(() ->
                        new NotFoundException(String.format(Error.Message.RESOURCE_NOT_FOUND_BY_ID, "Product", id))
                );
    }

    @Override
    public PaginationVm getProductCardVmByCategory(Long categoryId, Long brandId, String field, String dir, int page, int size) {
        Sort sort = checkAndReturnSort(field, dir);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Product> listProduct = productRepository.findByCategoryIdAndIsPublishedIsTrue(categoryId, brandId, pageable);
        List<ProductCardVm> productCardVmList = listProduct
                .stream().map(
                        productVm -> {
                            Optional<ProductGallery> optional = productGalleryRepository.findByProductIdAndThumbnailTrue(productVm.getId());
                            String thumbnail = null;
                            if (optional.isPresent()) {
                                thumbnail = optional.get().getImagePath();
                            }
                            return new ProductCardVm(
                                    productVm.getId(),
                                    productVm.getProductName(),
                                    productVm.getPrice(),
                                    thumbnail
                            );
                        }
                ).toList();

        return new PaginationVm(
                listProduct.getTotalPages(),
                listProduct.getTotalElements(),
                listProduct.getSize(),
                listProduct.getNumber(),
                productCardVmList
        );
    }

    @Override
    @Transactional
    public void createProduct(ProductPostVm productPostVm) {
        Product newProduct = ProductMapper.INSTANCE.toProduct(productPostVm);

        if (productPostVm.categoryId() != null) {
            Category category = categoryRepository.findById(productPostVm.categoryId())
                    .orElseThrow(
                            () -> new NotFoundException(String.format(Error.Message.RESOURCE_NOT_FOUND_BY_ID, "Category", productPostVm.categoryId()))
                    );
            newProduct.setCategory(category);
        }
        if (productPostVm.brandId() != null) {
            Brand brand = brandRepository.findById(productPostVm.brandId())
                    .orElseThrow(
                            () -> new NotFoundException(String.format(Error.Message.RESOURCE_NOT_FOUND_BY_ID, "Brand", productPostVm.categoryId()))
                    );
            newProduct.setBrand(brand);
        }

        productRepository.save(newProduct);

        if (!productPostVm.productImageUrls().isEmpty()) {
            productPostVm.productImageUrls().forEach(url -> {
                ProductGallery productGallery = new ProductGallery();
                productGallery.setImagePath(url);
                productGallery.setProduct(newProduct);
                productGalleryRepository.save(productGallery);
            });
        }
    }

    private Sort checkAndReturnSort(String field, String dir) {
        String price = "price";
        if (dir.equalsIgnoreCase("asc") && field.equalsIgnoreCase(price)) return Sort.by(Sort.Direction.ASC, price);
        return Sort.by(Sort.Direction.DESC, price);
    }
}
