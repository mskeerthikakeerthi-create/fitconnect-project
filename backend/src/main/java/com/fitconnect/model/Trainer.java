package com.fitconnect.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Trainer Entity - Extended profile for Fitness Professionals
 * Linked 1-to-1 with User entity
 */
@Entity
@Table(name = "trainers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Trainer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // One-to-one relationship with User
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private int experienceYears; // Years of experience

    // Skills stored as comma-separated string (e.g., "Yoga,HIIT,Strength Training")
    @Column(columnDefinition = "TEXT")
    private String skills;

    // Certifications (e.g., "NASM CPT,ACE,CrossFit Level 1")
    @Column(columnDefinition = "TEXT")
    private String certifications;

    private Double hourlyRate;  // Price per hour in USD
    private String location;    // City/Area
    private String specialization; // e.g., "Weight Loss", "Bodybuilding"

    private Double averageRating = 0.0; // Calculated average rating
    private Integer totalReviews = 0;   // Total number of reviews
    private Boolean isAvailable = true; // Currently accepting clients

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // Helper: Parse skills string into list
    @Transient
    public List<String> getSkillList() {
        if (skills == null || skills.isEmpty()) return List.of();
        return List.of(skills.split(","));
    }
}
