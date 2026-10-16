package com.example.demo.controller;

import com.example.demo.model.ProductReview;
import com.example.demo.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService service;

    @PostMapping
    public ProductReview create(@RequestBody ProductReview review) {
        return service.addReview(review);
    }

    @GetMapping("/product/{productId}")
    public List<ProductReview> getByProduct(@PathVariable Long productId) {
        return service.getReviewsByProduct(productId);
    }

    @PutMapping("/{id}")
    public ProductReview update(@PathVariable String id, @RequestBody ProductReview review) {
        return service.updateReview(id, review);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        service.deleteReview(id);
    }
}