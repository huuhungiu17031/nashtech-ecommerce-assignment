package com.nashtech.cellphonesfake.controller;

import com.nashtech.cellphonesfake.constant.Message;
import com.nashtech.cellphonesfake.service.ProductService;
import com.nashtech.cellphonesfake.view.PaginationVm;
import com.nashtech.cellphonesfake.view.ProductCardVm;
import com.nashtech.cellphonesfake.view.ProductDetailAdminVm;
import com.nashtech.cellphonesfake.view.ProductDetailVm;
import com.nashtech.cellphonesfake.view.ProductPostVm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("/{categoryId}")
    public ResponseEntity<PaginationVm> getListProduct(@PathVariable Long categoryId,
                                                       @RequestParam(required = false, defaultValue = "price") String field,
                                                       @RequestParam(required = false, defaultValue = "desc") String dir,
                                                       @RequestParam(required = false) Long brandId,
                                                       @RequestParam(required = false, defaultValue = "0") int page,
                                                       @RequestParam(required = false, defaultValue = "10") int size
    ) {
        return new ResponseEntity<>(productService.getProductCardVmByCategory(categoryId, brandId, field, dir, page, size), HttpStatus.OK);
    }

    @GetMapping("/backoffice/product-detail/{id}")
    public ResponseEntity<ProductDetailAdminVm> getProductDetailAdmin(@PathVariable Long id) {
        return new ResponseEntity<>(productService.getProductDetailAdmin(id), HttpStatus.OK);
    }

    @GetMapping("/product-detail/{id}")
    public ResponseEntity<ProductDetailVm> getProductDetail(@PathVariable Long id) {
        ProductDetailVm productDetailVmList = productService.getProductDetail(id);
        return new ResponseEntity<>(productDetailVmList, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> createProduct(@Valid @RequestBody ProductPostVm productPostVm) {
        productService.createProduct(productPostVm);
        return new ResponseEntity<>(Message.PRODUCT_CREATED, HttpStatus.CREATED);
    }


    @GetMapping("/backoffice")
    public ResponseEntity<PaginationVm> getListProductBackOffice(
            @RequestParam(required = false, defaultValue = "price") String field,
            @RequestParam(required = false, defaultValue = "desc") String dir,
            @RequestParam(required = false) Long brandId,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size
    ) {
        return new ResponseEntity<>(productService.getProductCardAdminVmByCategory(brandId, field, dir, page, size), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<String> updateProduct(@Valid @RequestBody ProductPostVm productPostVm) {
        productService.updateProduct(productPostVm);
        return new ResponseEntity<>("Edit product successfully", HttpStatus.OK);
    }

    @PostMapping("/findProductByImage")
    public ResponseEntity<List<ProductCardVm>> getProductByImage(@RequestParam("image") MultipartFile image) throws IOException {
            return new ResponseEntity<>(productService.findProductByImage(image), HttpStatus.OK);
    }
}
