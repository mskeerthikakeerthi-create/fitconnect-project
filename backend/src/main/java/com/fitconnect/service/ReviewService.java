package com.fitconnect.service;

import com.fitconnect.dto.ReviewDTO;
import com.fitconnect.model.*;
import com.fitconnect.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * ReviewService - Manages reviews and ratings for trainers
 */
@Service
public class ReviewService {

    @Autowired private ReviewRepository reviewRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private TrainerRepository trainerRepository;

    /** Get all reviews for a trainer */
    public List<Review> getTrainerReviews(Long trainerId) {
        Trainer trainer = trainerRepository.findById(trainerId)
            .orElseThrow(() -> new RuntimeException("Trainer not found"));
        return reviewRepository.findByTrainer(trainer);
    }

    /** Submit a review for a trainer */
    public Review createReview(String userEmail, ReviewDTO dto) {
        User user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new RuntimeException("User not found"));
        Trainer trainer = trainerRepository.findById(dto.getTrainerId())
            .orElseThrow(() -> new RuntimeException("Trainer not found"));

        // Check if already reviewed
        if (reviewRepository.findByUserAndTrainer(user, trainer).isPresent()) {
            throw new RuntimeException("You have already reviewed this trainer");
        }

        Review review = new Review();
        review.setUser(user);
        review.setTrainer(trainer);
        review.setRating(dto.getRating());
        review.setComment(dto.getComment());

        Review saved = reviewRepository.save(review);

        // Update trainer's average rating
        updateTrainerRating(trainer);

        return saved;
    }

    /** Delete a review */
    public void deleteReview(Long reviewId, String userEmail) {
        Review review = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new RuntimeException("Review not found"));

        if (!review.getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("Unauthorized: Not your review");
        }

        Trainer trainer = review.getTrainer();
        reviewRepository.delete(review);
        updateTrainerRating(trainer);
    }

    // Recalculate and update trainer's average rating
    private void updateTrainerRating(Trainer trainer) {
        Double avg = reviewRepository.findAverageRatingByTrainer(trainer);
        long count = reviewRepository.countByTrainer(trainer);
        trainer.setAverageRating(avg != null ? Math.round(avg * 10.0) / 10.0 : 0.0);
        trainer.setTotalReviews((int) count);
        trainerRepository.save(trainer);
    }
}
