package com.nashtech.cellphonesfake.service.impl;

import com.nashtech.cellphonesfake.model.Rating;
import com.nashtech.cellphonesfake.repository.RatingRepository;
import com.nashtech.cellphonesfake.service.ProductService;
import com.nashtech.cellphonesfake.service.RatingService;
import com.nashtech.cellphonesfake.view.RatingVm;
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
    public List<RatingVm> getRatingByProductId(Long productId) {
        productService.findProductById(productId);
        return mapRatings(ratingRepository.findByProduct_Id(productId));
    }

    @Override
    public void deleteRating(Long id) {
        ratingRepository.deleteById(id);
    }

    @Override
    public List<RatingVm> getPublishRating(Long productId) {
        productService.findProductById(productId);
        return mapRatings(ratingRepository.findByProduct_IdAndIsPublishIsTrue(productId));
    }

    private List<RatingVm> mapRatings(List<Rating> ratings) {
        return ratings.stream().map(rating ->
                new RatingVm(
                        rating.getComment(),
                        rating.getScore(),
                        rating.getProduct().getId()
                )
        ).toList();
    }
}
