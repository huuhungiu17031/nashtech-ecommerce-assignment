package com.nashtech.cellphonesfake.controller;
import com.nashtech.cellphonesfake.service.ProductGalleryService;
import com.nashtech.cellphonesfake.view.ProductGalleryVm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductGalleryControllerTest {
    @Mock
    private ProductGalleryService productGalleryService;

    @InjectMocks
    private ProductGalleryController productGalleryController;
    @Captor
    private ArgumentCaptor<Long> productIdCaptor;
    @Captor
    private ArgumentCaptor<Long> idCaptor;
    private ProductGalleryVm productGalleryVm1;
    private ProductGalleryVm productGalleryVm2;

    private static final Long PRODUCT_ID = 1L;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        productGalleryController = new ProductGalleryController(productGalleryService);
        productGalleryVm1 = new ProductGalleryVm(1L, true, "path");
        productGalleryVm2 = new ProductGalleryVm(2L, true, "path");
    }


    @Test
    void testGetProductGalleryByProductId_Success() {
        // Given
        Long productId = 1L;
        List<ProductGalleryVm> expectedProductGallery = Arrays.asList(productGalleryVm1, productGalleryVm2);
        when(productGalleryService.getListGalleryByProductId(anyLong())).thenReturn(expectedProductGallery);

        // When
        ResponseEntity<List<ProductGalleryVm>> responseEntity = productGalleryController.getProductGalleryByProductId(productId);

        // Then
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedProductGallery, responseEntity.getBody());
        verify(productGalleryService).getListGalleryByProductId(productIdCaptor.capture());
        assertEquals(productId, productIdCaptor.getValue());
    }

    @Test
    void testGetProductGalleryInCart_whenProductExists_shouldReturnProductGalleryVm() {
        // Given
        Long productId = 1L;
        when(productGalleryService.getListGalleryInCart(anyLong())).thenReturn(productGalleryVm1);

        // When
        ResponseEntity<ProductGalleryVm> responseEntity = productGalleryController.getProductGalleryInCart(productId);

        // Then
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(productGalleryVm1, responseEntity.getBody());
        verify(productGalleryService).getListGalleryInCart(productIdCaptor.capture());
        assertEquals(productId, productIdCaptor.getValue());
    }
}
