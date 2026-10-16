package com.example.demo.service;

import com.example.demo.model.ProductReview;
import com.example.demo.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository repository;

    public ProductReview addReview(ProductReview review) {
        return repository.save(review);
    }

    public List<ProductReview> getReviewsByProduct(Long productId) {
        return repository.findByProductId(productId);
    }

    public ProductReview updateReview(String id, ProductReview updatedReview) {
        ProductReview existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reseña no encontrada"));

        existing.setComment(updatedReview.getComment());
        existing.setRating(updatedReview.getRating());
        return repository.save(existing);
    }

    public void deleteReview(String id) {
        repository.deleteById(id);
    }
}