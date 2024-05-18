package com.nashtech.cellphonesfake.service.impl;

import com.nashtech.cellphonesfake.constant.Error;
import com.nashtech.cellphonesfake.enumeration.PaymentMethod;
import com.nashtech.cellphonesfake.exception.BadRequestException;
import com.nashtech.cellphonesfake.exception.NotFoundException;
import com.nashtech.cellphonesfake.mapper.ProductMapper;
import com.nashtech.cellphonesfake.model.Brand;
import com.nashtech.cellphonesfake.model.Category;
import com.nashtech.cellphonesfake.model.Product;
import com.nashtech.cellphonesfake.model.ProductGallery;
import com.nashtech.cellphonesfake.repository.ProductGalleryRepository;
import com.nashtech.cellphonesfake.repository.ProductRepository;
import com.nashtech.cellphonesfake.service.BrandService;
import com.nashtech.cellphonesfake.service.CategoryService;
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

import java.lang.invoke.MethodType;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductGalleryRepository productGalleryRepository;
    private final CategoryService categoryService;
    private final BrandService brandService;

    public ProductServiceImpl(ProductRepository productRepository, ProductGalleryRepository productGalleryRepository, CategoryService categoryService, BrandService brandService) {
        this.productRepository = productRepository;
        this.productGalleryRepository = productGalleryRepository;
        this.categoryService = categoryService;
        this.brandService = brandService;
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
            Category category = categoryService.findCategoryById(productPostVm.categoryId());
            newProduct.setCategory(category);
        }
        if (productPostVm.brandId() != null) {
            Brand brand = brandService.findBrandById(productPostVm.brandId());
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

    @Override
    public Product findProductById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new NotFoundException(String.format(Error.Message.RESOURCE_NOT_FOUND_BY_ID, "Product", id)));
    }

    @Transactional
    @Override
    public Product checkProductAmountAndReduceStockQuantity(Long productId, Long amount, PaymentMethod paymentMethod) {
        Product product = findProductById(productId);
        if (product.getStockQuantity() == 0) {
            product.setAvailable(false);
            productRepository.save(product);
            throw new BadRequestException("Product is out of stock");
        }
        if (amount < product.getStockQuantity() && paymentMethod.equals(PaymentMethod.SHIP_CODE)) {
            Long newStockQuantity = product.getStockQuantity() - amount;
            product.setStockQuantity(newStockQuantity);
            return productRepository.save(product);
        }
        if (amount < product.getStockQuantity() && paymentMethod.equals(PaymentMethod.VN_PAY)) return product;
        throw new BadRequestException("Invalid product amount");
    }

    @Override
    public void saveProduct(Product product) {
        productRepository.save(product);
    }

    private Sort checkAndReturnSort(String field, String dir) {
        String price = "price";
        if (dir.equalsIgnoreCase("asc") && field.equalsIgnoreCase(price)) return Sort.by(Sort.Direction.ASC, price);
        return Sort.by(Sort.Direction.DESC, price);
    }
}
