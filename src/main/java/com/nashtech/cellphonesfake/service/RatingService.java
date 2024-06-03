package com.nashtech.cellphonesfake.service;

import com.nashtech.cellphonesfake.view.PaginationVm;
import com.nashtech.cellphonesfake.view.RatingVm;

public interface RatingService {
    void createRating(RatingVm ratingVm);
//    List<RatingVm> getRatingByProductId(Long productId, int page, int size);
    void deleteRating(Long id);
    PaginationVm getPublishRating(Long productId, int page, int size);
    Double getAverageRating(Long productId);
}
