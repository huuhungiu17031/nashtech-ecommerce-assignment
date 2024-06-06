package com.nashtech.cellphonesfake.controller;

import com.nashtech.cellphonesfake.constant.Message;
import com.nashtech.cellphonesfake.model.Product;
import com.nashtech.cellphonesfake.model.Rating;
import com.nashtech.cellphonesfake.service.RatingService;
import com.nashtech.cellphonesfake.view.PaginationVm;
import com.nashtech.cellphonesfake.view.RatingVm;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RatingControllerTest {

    @Mock
    private RatingService ratingService;
    private Rating rating1;
    private Product product;
    private RatingVm ratingVm;
    @Captor
    private ArgumentCaptor<Long> idCaptor;
    @InjectMocks
    private RatingController ratingController;
    @Captor
    private ArgumentCaptor<RatingVm> ratingVmCaptor;

    private static final Long PRODUCT_ID = 1L;
    private static final Double AVERAGE_RATING = 4.5;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        product = new Product();
        product.setId(1L);
        rating1 = new Rating();
        rating1.setId(1L);
        rating1.setComment("Great product!");
        rating1.setScore(4);
        rating1.setProduct(product); // Assuming Product class exists
        rating1.setCreatedBy("user1");
        ratingVm = new RatingVm(rating1.getComment(), rating1.getScore(), product.getId(), rating1.getCreatedBy());
        ratingController = new RatingController(ratingService);

    }

    @Test
    void createRating_shouldReturnOkResponseAndCallService() {
        // When
        ResponseEntity<String> responseEntity = ratingController.createRating(ratingVm);
        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Message.RATING_CREATED, responseEntity.getBody());
        verify(ratingService).createRating(ratingVmCaptor.capture());
        assertEquals(ratingVm, ratingVmCaptor.getValue());
    }


    @Test
    void testGetPublishedRatingsByProductId_ShouldReturnPaginationVm() {
        PaginationVm paginationVm = new PaginationVm(2, 10L, 10, 0, List.of());
        // Arrange
        when(ratingService.getPublishRating(eq(1L), eq(0), eq(10))).thenReturn(paginationVm);

        // Act
        ResponseEntity<PaginationVm> responseEntity = ratingController.getPublishedRatingsByProductId(1L, 0, 10);

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(paginationVm, responseEntity.getBody());
    }

    @Test
    void deleteRating_whenIdExists_shouldReturnSuccessMessage() {
        // Arrange
        Long ratingId = 1L;
        doNothing().when(ratingService).deleteRating(eq(ratingId));
        // Act
        ResponseEntity<String> responseEntity = ratingController.deleteRating(ratingId);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Message.RATING_DELETED, responseEntity.getBody());
        verify(ratingService).deleteRating(idCaptor.capture());
        assertEquals(ratingId, idCaptor.getValue());
    }
    @Test
    void testGetAverageRating_Success() {
        // Given
        when(ratingService.getAverageRating(anyLong())).thenReturn(AVERAGE_RATING);

        // When
        ResponseEntity<Double> responseEntity = ratingController.getAverageRating(PRODUCT_ID);

        // Then
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(AVERAGE_RATING, responseEntity.getBody());
    }
}
