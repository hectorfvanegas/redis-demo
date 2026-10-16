package com.example.demo.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "reviews")
@Getter
@Setter
public class ProductReview {
    @Id
    private String id;

    private Long productId;
    private String author;
    private String comment;
    private Integer rating;
}