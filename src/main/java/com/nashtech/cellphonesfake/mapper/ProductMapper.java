package com.nashtech.cellphonesfake.mapper;

import com.nashtech.cellphonesfake.model.Product;
import com.nashtech.cellphonesfake.view.PaginationVm;
import com.nashtech.cellphonesfake.view.ProductAdminVm;
import com.nashtech.cellphonesfake.view.ProductDetailVm;
import com.nashtech.cellphonesfake.view.ProductPostVm;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

@Mapper
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    Product toProduct(ProductPostVm productPostVm);

    ProductDetailVm toProductDetailVm(Product product);

    ProductAdminVm toProductAdminVm(Product product);

    PaginationVm toPaginationVm(Page<Product> products);
}
