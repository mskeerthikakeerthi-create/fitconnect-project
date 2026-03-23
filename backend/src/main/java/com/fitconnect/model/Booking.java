package com.fitconnect.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Booking Entity - Represents a session booking between User and Trainer
 */
@Entity
@Table(name = "bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The client who booked
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // The trainer being booked
    @ManyToOne
    @JoinColumn(name = "trainer_id", nullable = false)
    private Trainer trainer;

    @Column(nullable = false)
    private LocalDate sessionDate; // Date of the session

    private String sessionTime;    // e.g., "10:00 AM"
    private String sessionType;    // e.g., "Online", "In-Person"
    private String notes;          // Special requests from user

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status = BookingStatus.PENDING;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Booking status lifecycle: PENDING -> ACCEPTED or REJECTED -> COMPLETED
    public enum BookingStatus {
        PENDING,   // Awaiting trainer response
        ACCEPTED,  // Trainer accepted
        REJECTED,  // Trainer rejected
        COMPLETED, // Session done
        CANCELLED  // User cancelled
    }
}
