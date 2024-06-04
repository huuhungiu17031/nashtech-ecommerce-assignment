package com.nashtech.cellphonesfake.controller;

import com.nashtech.cellphonesfake.constant.Message;
import com.nashtech.cellphonesfake.service.RatingService;
import com.nashtech.cellphonesfake.view.PaginationVm;
import com.nashtech.cellphonesfake.view.RatingVm;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rating")
public class RatingController {
    private final RatingService ratingService;

    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @PostMapping
    public ResponseEntity<String> createRating(@RequestBody RatingVm ratingVm) {
        ratingService.createRating(ratingVm);
        return new ResponseEntity<>(Message.RATING_CREATED, HttpStatus.OK);
    }

    @GetMapping("/store-front/{productId}")
    public ResponseEntity<PaginationVm> getPublishedRatingsByProductId(
            @PathVariable Long productId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        return new ResponseEntity<>(ratingService.getPublishRating(productId, page, size), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRating(@PathVariable Long id) {
        ratingService.deleteRating(id);
        return new ResponseEntity<>(Message.RATING_DELETED, HttpStatus.OK);
    }

    @GetMapping("/average/{productId}")
    public ResponseEntity<Double> getAverageRating(@PathVariable Long productId) {
        return new ResponseEntity<>(ratingService.getAverageRating(productId), HttpStatus.OK);
    }
}
