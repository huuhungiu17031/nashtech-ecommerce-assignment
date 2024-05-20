package com.nashtech.cellphonesfake.service;

import com.nashtech.cellphonesfake.view.RatingVm;

import java.util.List;

public interface RatingService {
    void createRating(RatingVm ratingVm);
    List<RatingVm> getRatingByProductId(Long productId);
    void deleteRating(Long id);
    List<RatingVm> getPublishRating(Long productId);
}
