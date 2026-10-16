package com.example.demo.repository;

import com.example.demo.model.ProductReview;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends MongoRepository<ProductReview, String> {
    List<ProductReview> findByProductId(Long productId);
}