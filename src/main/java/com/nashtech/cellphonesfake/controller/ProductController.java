package com.nashtech.cellphonesfake.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductController {

    @GetMapping("/{categoryId}")
    public ResponseEntity<String> getListProduct(@PathVariable Long categoryId,
                                                 @RequestParam(required = false, defaultValue = "price") String field,
                                                 @RequestParam(required = false, defaultValue = "desc") String dir,
                                                 @RequestParam(required = false) Long brandId,
                                                 @RequestParam(required = false, defaultValue = "0") int page,
                                                 @RequestParam(required = false, defaultValue = "2") int size
    ) {
        return new ResponseEntity<>("Hello", HttpStatus.OK);
    }
}
