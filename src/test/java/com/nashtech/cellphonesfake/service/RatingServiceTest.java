package com.nashtech.cellphonesfake.service;

import com.nashtech.cellphonesfake.model.Product;
import com.nashtech.cellphonesfake.model.Rating;
import com.nashtech.cellphonesfake.repository.RatingRepository;
import com.nashtech.cellphonesfake.service.impl.RatingServiceImpl;
import com.nashtech.cellphonesfake.view.PaginationVm;
import com.nashtech.cellphonesfake.view.RatingVm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RatingServiceTest {
    @Mock
    private RatingRepository ratingRepository;
    private Long productId;
    @Captor
    private ArgumentCaptor<Rating> ratingArgumentCaptor;
    @Mock
    private ProductService productService;
    @Captor
    private ArgumentCaptor<Long> idCaptor;
    @InjectMocks
    private RatingServiceImpl ratingService;

    private Product product;
    private Rating rating1;
    private Rating rating2;
    @BeforeEach
    public void setUp() {
        product = new Product();
        product.setId(1L);
        productId = 1L;
        ratingService = new RatingServiceImpl(ratingRepository, productService);
        rating1 = new Rating();
        rating1.setId(1L);
        rating1.setComment("Great product!");
        rating1.setScore(4);
        rating1.setProduct(product); // Assuming Product class exists
        rating1.setCreatedBy("user1");

        rating2 = new Rating();
        rating2.setId(2L);
        rating2.setComment("Not bad.");
        rating2.setScore(3);
        rating2.setProduct(product); // Assuming Product class exists
        rating2.setCreatedBy("user2");
    }

    @Test
    void testGetAverageRating_whenNoRatingsExist_returnsDefaultRating() {
        when(productService.findProductById(anyLong())).thenReturn(product);
        when(ratingRepository.getAverageRating(anyLong())).thenReturn(null);
        Double averageRating = ratingService.getAverageRating(1L);
        assertEquals(5.0, averageRating);
    }

    @Test
    void testGetAverageRating_whenRatingsExist_returnsAverageRating() {
        when(productService.findProductById(anyLong())).thenReturn(product);
        when(ratingRepository.getAverageRating(anyLong())).thenReturn(4.5);

        Double averageRating = ratingService.getAverageRating(1L);

        assertEquals(4.5, averageRating);
    }

    @Test
    void testGetPublishRating_NoRatingsFound_ReturnsCorrectEmptyPaginationVm() {
        // Arrange
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);
        Page<Rating> ratings = new PageImpl<>(Collections.emptyList(), pageable, 0);

        when(ratingRepository.findByProduct_Id(eq(productId), any(Pageable.class))).thenReturn(ratings);
        // Act
        PaginationVm paginationVm = ratingService.getPublishRating(productId, page, size);

        // Assert
        assertNotNull(paginationVm);
        assertEquals(0, paginationVm.totalPages());
        assertEquals(0, paginationVm.totalElements());
        assertEquals(size, paginationVm.size());
        assertEquals(page, paginationVm.currentPage());
        assertEquals(Collections.emptyList(), paginationVm.content());
    }

    @Test
    void createRating_shouldSaveRatingWithCorrectData() {
        // Given
        Long productId = 1L;
        String comment = "Great product!";
        int score = 4;
        Product product = new Product();
        product.setId(productId);

        RatingVm ratingVm = new RatingVm(comment, score, productId, "user1");

        when(productService.findProductById(productId)).thenReturn(product);

        // When
        ratingService.createRating(ratingVm);

        // Then
        verify(ratingRepository).save(ratingArgumentCaptor.capture());
        Rating capturedRating = ratingArgumentCaptor.getValue();
        assertEquals(comment, capturedRating.getComment());
        assertEquals(score, capturedRating.getScore());
        assertEquals(product, capturedRating.getProduct());
    }

    @Test
    void testMapRatings() throws Exception {
        List<Rating> ratings = List.of(rating1, rating2);

        Method mapRatingsMethod = RatingServiceImpl.class.getDeclaredMethod("mapRatings", List.class);
        mapRatingsMethod.setAccessible(true);

        @SuppressWarnings("unchecked")
        List<RatingVm> result = (List<RatingVm>) mapRatingsMethod.invoke(ratingService, ratings);

        assertEquals(2, result.size());

        assertEquals("Great product!", result.getFirst().comment());
        assertEquals(4, result.getFirst().score());
        assertEquals(1L, result.get(0).productId());
        assertEquals("user1", result.get(0).createdBy());

        assertEquals("Not bad.", result.get(1).comment());
        assertEquals(3, result.get(1).score());
        assertEquals(1L, result.get(1).productId());
        assertEquals("user2", result.get(1).createdBy());
    }

    @Test
    void deleteRating_whenIdExists_shouldDeleteRating() {
        // Arrange
        Long ratingId = 1L;

        // Act
        assertDoesNotThrow(() -> ratingService.deleteRating(ratingId));

        // Assert
        verify(ratingRepository).deleteById(idCaptor.capture());
        assert idCaptor.getValue().equals(ratingId);
    }
}
