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
import com.nashtech.cellphonesfake.specification.ProductSpecification;
import com.nashtech.cellphonesfake.view.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@Transactional
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductGalleryRepository productGalleryRepository;
    private final CategoryService categoryService;
    private final BrandService brandService;
    @Value("${python.service.url}")
    private String pythonServiceUrl;

    public ProductServiceImpl(
            ProductRepository productRepository,
            ProductGalleryRepository productGalleryRepository,
            CategoryService categoryService,
            BrandService brandService
    ) {
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
        Specification<Product> spec = ProductSpecification.withDynamicQueryParameters(categoryId, brandId, false);
        Page<Product> listProduct = productRepository.findAll(spec, pageable);
        List<ProductCardVm> productCardVmList = convertProductToProductCardVm(listProduct.getContent());
        return new PaginationVm(
                listProduct.getTotalPages(),
                listProduct.getTotalElements(),
                listProduct.getSize(),
                listProduct.getNumber(),
                productCardVmList
        );
    }

    @Override
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
        AtomicBoolean isFirstGallery = new AtomicBoolean(true);

        if (!productPostVm.productImageUrls().isEmpty()) {
            productPostVm.productImageUrls().forEach(url -> {
                ProductGallery productGallery = new ProductGallery();
                productGallery.setImagePath(url);
                productGallery.setProduct(newProduct);
                if (isFirstGallery.get()) {
                    productGallery.setThumbnail(true);
                    isFirstGallery.set(false);
                }
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
    public PaginationVm getProductCardAdminVmByCategory(Long brandId, String field, String dir, int page, int size) {
        Sort sort = checkAndReturnSort(field, dir);
        Pageable pageable = PageRequest.of(page, size, sort);
        Specification<Product> spec = ProductSpecification.withDynamicQueryParameters(null, brandId, false);
        Page<Product> listProduct = productRepository.findAll(spec, pageable);
        return new PaginationVm(
                listProduct.getTotalPages(),
                listProduct.getTotalElements(),
                listProduct.getSize(),
                listProduct.getNumber(),
                listProduct.stream().map(product -> {
                    Optional<ProductGallery> productGallery = productGalleryRepository.findByProductIdAndThumbnailTrue(product.getId());
                    return productGallery.map(gallery -> new ProductAdminVm(
                            product.getId(),
                            product.getProductName(),
                            product.getPrice(),
                            product.getStockQuantity(),
                            product.getIsFeatured(),
                            gallery.getImagePath()
                    )).orElseGet(() -> new ProductAdminVm(
                            product.getId(),
                            product.getProductName(),
                            product.getPrice(),
                            product.getStockQuantity(),
                            product.getIsFeatured(),
                            null
                    ));
                }).toList()
        );
    }

    @Override
    public ProductDetailAdminVm getProductDetailAdmin(Long id) {
        Product product = findProductById(id);
        return new ProductDetailAdminVm(
                product.getId(),
                product.getProductName(),
                product.getPrice(),
                product.getStockQuantity(),
                product.getDescription(),
                product.getIsFeatured(),
                product.getBrand().getId(),
                product.getCategory().getId()
        );
    }

    @Override
    public void updateProduct(ProductPostVm productPostVm) {
        Product product = findProductById(productPostVm.id());
        product.setProductName(productPostVm.productName());
        product.setPrice(productPostVm.price());
        product.setStockQuantity(productPostVm.stockQuantity());
        product.setDescription(productPostVm.description());
        product.setIsFeatured(productPostVm.isFeatured());
        productGalleryRepository.deleteAllByProduct_Id(product.getId());
        AtomicBoolean isFirstGallery = new AtomicBoolean(true);
        productPostVm.productImageUrls().forEach(path -> {
            ProductGallery productGallery = new ProductGallery();
            productGallery.setProduct(product);
            productGallery.setImagePath(path);
            if (isFirstGallery.get()) {
                productGallery.setThumbnail(true);
                isFirstGallery.set(false);
            }
            productGalleryRepository.save(productGallery);
        });
        Category category = categoryService.findCategoryById(productPostVm.categoryId());
        Brand brand = brandService.findBrandById(productPostVm.brandId());
        product.setCategory(category);
        product.setBrand(brand);
        productRepository.save(product);
    }

    @Override
    public List<ProductCardVm> findProductByImage(MultipartFile image) throws IOException {
        try {
            RestTemplate restTemplate = new RestTemplate();
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("image", new MultipartInputStreamFileResource(image.getInputStream(), image.getOriginalFilename()));
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(pythonServiceUrl, requestEntity, String.class);
            List<String> keywordsList = extractKeywordsFromResponse(Objects.requireNonNull(response.getBody()));
            Set<Product> recommendProducts = new HashSet<>();
            for (String keyword : keywordsList) {
                log.info("Keyword: {}", keyword);
                List<Product> products = productRepository.findByProductNameContainsIgnoreCaseOrDescriptionContainingIgnoreCase(keyword.toLowerCase(), keyword.toLowerCase());
                recommendProducts.addAll(products);
                if (products.size() == 1) {
                    break;
                }
            }
            return convertProductToProductCardVm(recommendProducts.stream().toList());
        } catch (IOException io) {
            throw new IOException(io.getMessage());
        }

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

    private static class MultipartInputStreamFileResource extends InputStreamResource {
        private final String filename;

        MultipartInputStreamFileResource(InputStream inputStream, String filename) {
            super(inputStream);
            this.filename = filename;
        }

        @Override
        public String getFilename() {
            return this.filename;
        }

        @Override
        public long contentLength() throws IOException {
            return -1; // We do not want to generally read the whole stream into memory ...
        }
    }

    private static List<String> extractKeywordsFromResponse(String responseBody) {
        String keywordsString = responseBody.substring(responseBody.indexOf("'") + 1, responseBody.lastIndexOf("'"));
        String[] keywordsArray = keywordsString.split(", ");
        return Arrays.asList(keywordsArray);
    }

    private List<ProductCardVm> convertProductToProductCardVm(List<Product> products) {
        return products.stream().map(
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
                            thumbnail,
                            productVm.getIsFeatured()
                    );
                }
        ).toList();
    }


}
