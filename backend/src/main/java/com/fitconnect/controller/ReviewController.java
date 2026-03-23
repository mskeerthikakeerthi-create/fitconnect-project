package com.fitconnect.controller;

import com.fitconnect.dto.*;
import com.fitconnect.model.Review;
import com.fitconnect.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

/**
 * ReviewController - Trainer review and rating system
 * Base URL: /api/reviews
 */
@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "*")
public class ReviewController {

    @Autowired private ReviewService reviewService;

    /**
     * GET /api/reviews/trainer/{trainerId}
     * Get all reviews for a trainer (public)
     */
    @GetMapping("/trainer/{trainerId}")
    public ResponseEntity<?> getTrainerReviews(@PathVariable Long trainerId) {
        List<Review> reviews = reviewService.getTrainerReviews(trainerId);
        return ResponseEntity.ok(ApiResponse.success("Reviews fetched", reviews));
    }

    /**
     * POST /api/reviews
     * Submit a review for a trainer
     */
    @PostMapping
    public ResponseEntity<?> createReview(@RequestBody ReviewDTO dto, Principal principal) {
        try {
            Review review = reviewService.createReview(principal.getName(), dto);
            return ResponseEntity.ok(ApiResponse.success("Review submitted!", review));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * DELETE /api/reviews/{id}
     * Delete your review
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReview(@PathVariable Long id, Principal principal) {
        try {
            reviewService.deleteReview(id, principal.getName());
            return ResponseEntity.ok(ApiResponse.success("Review deleted", null));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}
