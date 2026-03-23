package com.fitconnect.repository;

import com.fitconnect.model.Review;
import com.fitconnect.model.Trainer;
import com.fitconnect.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * ReviewRepository - JPA Data Access Layer for Review entity
 */
@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    // Get all reviews for a trainer
    List<Review> findByTrainer(Trainer trainer);

    // Get review by a specific user for a specific trainer
    Optional<Review> findByUserAndTrainer(User user, Trainer trainer);

    // Calculate average rating for a trainer
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.trainer = :trainer")
    Double findAverageRatingByTrainer(@Param("trainer") Trainer trainer);

    // Count total reviews for a trainer
    long countByTrainer(Trainer trainer);
}
