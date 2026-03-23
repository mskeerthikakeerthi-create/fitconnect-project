package com.fitconnect.repository;

import com.fitconnect.model.Trainer;
import com.fitconnect.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * TrainerRepository - JPA Data Access Layer for Trainer entity
 */
@Repository
public interface TrainerRepository extends JpaRepository<Trainer, Long> {

    // Find trainer profile by their User account
    Optional<Trainer> findByUser(User user);

    // Search trainers by location (case-insensitive)
    List<Trainer> findByLocationContainingIgnoreCase(String location);

    // Search trainers by skills (skills is stored as comma-separated string)
    List<Trainer> findBySkillsContainingIgnoreCase(String skill);

    // Filter by location AND skill
    List<Trainer> findByLocationContainingIgnoreCaseAndSkillsContainingIgnoreCase(
        String location, String skill);

    // Filter by max hourly rate
    List<Trainer> findByHourlyRateLessThanEqual(Double maxRate);

    // Filter by minimum experience
    List<Trainer> findByExperienceYearsGreaterThanEqual(Integer minYears);

    // Custom query: Search by location OR skill, sorted by rating
    @Query("SELECT t FROM Trainer t WHERE " +
           "(:location IS NULL OR LOWER(t.location) LIKE LOWER(CONCAT('%', :location, '%'))) AND " +
           "(:skill IS NULL OR LOWER(t.skills) LIKE LOWER(CONCAT('%', :skill, '%'))) AND " +
           "(:maxRate IS NULL OR t.hourlyRate <= :maxRate) AND " +
           "(:minExp IS NULL OR t.experienceYears >= :minExp) " +
           "ORDER BY t.averageRating DESC")
    List<Trainer> searchTrainers(
        @Param("location") String location,
        @Param("skill") String skill,
        @Param("maxRate") Double maxRate,
        @Param("minExp") Integer minExp);
}
