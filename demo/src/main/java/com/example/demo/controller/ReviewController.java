package com.example.demo.controller;

import com.example.demo.dto.ReviewDTO;
import com.example.demo.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewDTO> createReview(@RequestBody ReviewDTO reviewDTO) {
        try {
            ReviewDTO created = reviewService.createReview(reviewDTO);
            return ResponseEntity.ok(created);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/location/{locationId}")
    public ResponseEntity<List<ReviewDTO>> getReviewsByLocation(@PathVariable Long locationId) {
        return ResponseEntity.ok(reviewService.getReviewsByLocation(locationId));
    }

    @GetMapping("/user/{email}")
    public ResponseEntity<List<ReviewDTO>> getReviewsByUser(@PathVariable String email) {
        return ResponseEntity.ok(reviewService.getReviewsByUser(email));
    }
}

