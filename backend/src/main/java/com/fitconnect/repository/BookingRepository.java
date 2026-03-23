package com.fitconnect.repository;

import com.fitconnect.model.Booking;
import com.fitconnect.model.Booking.BookingStatus;
import com.fitconnect.model.Trainer;
import com.fitconnect.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * BookingRepository - JPA Data Access Layer for Booking entity
 */
@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    // Get all bookings made by a specific user
    List<Booking> findByUser(User user);

    // Get all bookings for a specific trainer
    List<Booking> findByTrainer(Trainer trainer);

    // Get bookings filtered by status
    List<Booking> findByUserAndStatus(User user, BookingStatus status);
    List<Booking> findByTrainerAndStatus(Trainer trainer, BookingStatus status);

    // Check if user already has a booking with this trainer on this date
    boolean existsByUserAndTrainerAndSessionDate(User user, Trainer trainer,
        java.time.LocalDate sessionDate);
}
