package com.nashtech.cellphonesfake.service.impl;

import com.nashtech.cellphonesfake.model.Rating;
import com.nashtech.cellphonesfake.repository.RatingRepository;
import com.nashtech.cellphonesfake.service.ProductService;
import com.nashtech.cellphonesfake.service.RatingService;
import com.nashtech.cellphonesfake.view.PaginationVm;
import com.nashtech.cellphonesfake.view.RatingVm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RatingServiceImpl implements RatingService {
    private final RatingRepository ratingRepository;
    private final ProductService productService;

    public RatingServiceImpl(RatingRepository ratingRepository, ProductService productService) {
        this.ratingRepository = ratingRepository;
        this.productService = productService;
    }

    @Override
    public void createRating(RatingVm rating) {
        Rating newRating = new Rating();
        newRating.setComment(rating.comment());
        newRating.setScore(rating.score());
        newRating.setProduct(productService.findProductById(rating.productId()));
        ratingRepository.save(newRating);
    }

    @Override
    public void deleteRating(Long id) {
        ratingRepository.deleteById(id);
    }

    @Override
    public PaginationVm getPublishRating(Long productId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Rating> ratings = ratingRepository.findByProduct_Id(productId, pageable);
        return new PaginationVm(
                ratings.getTotalPages(),
                ratings.getTotalElements(),
                ratings.getSize(),
                ratings.getNumber(),
                mapRatings(ratings.stream().toList())
        );
    }

    @Override
    public Double getAverageRating(Long productId) {
        productService.findProductById(productId);
        Double ratingAverage = ratingRepository.getAverageRating(productId);
        if (ratingAverage == null) return 5.0;
        return ratingAverage;
    }

    private List<RatingVm> mapRatings(List<Rating> ratings) {
        return ratings.stream().map(rating ->
                new RatingVm(
                        rating.getComment(),
                        rating.getScore(),
                        rating.getProduct().getId(),
                        rating.getCreatedBy()
                )
        ).toList();
    }
}
