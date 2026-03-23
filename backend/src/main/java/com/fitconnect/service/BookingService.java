package com.fitconnect.service;

import com.fitconnect.dto.BookingDTO;
import com.fitconnect.model.*;
import com.fitconnect.model.Booking.BookingStatus;
import com.fitconnect.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * BookingService - Handles session booking logic
 */
@Service
public class BookingService {

    @Autowired private BookingRepository bookingRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private TrainerRepository trainerRepository;

    /** User creates a booking request */
    public Booking createBooking(String userEmail, BookingDTO dto) {
        User user = getUser(userEmail);
        Trainer trainer = trainerRepository.findById(dto.getTrainerId())
            .orElseThrow(() -> new RuntimeException("Trainer not found"));

        // Prevent duplicate bookings on same date
        if (bookingRepository.existsByUserAndTrainerAndSessionDate(
                user, trainer, dto.getSessionDate())) {
            throw new RuntimeException("You already have a booking with this trainer on this date");
        }

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setTrainer(trainer);
        booking.setSessionDate(dto.getSessionDate());
        booking.setSessionTime(dto.getSessionTime());
        booking.setSessionType(dto.getSessionType());
        booking.setNotes(dto.getNotes());
        booking.setStatus(BookingStatus.PENDING);

        return bookingRepository.save(booking);
    }

    /** Get all bookings for current user */
    public List<Booking> getUserBookings(String email) {
        return bookingRepository.findByUser(getUser(email));
    }

    /** Get all bookings for a trainer */
    public List<Booking> getTrainerBookings(String email) {
        User user = getUser(email);
        Trainer trainer = trainerRepository.findByUser(user)
            .orElseThrow(() -> new RuntimeException("Trainer profile not found"));
        return bookingRepository.findByTrainer(trainer);
    }

    /** Trainer accepts or rejects a booking */
    public Booking updateBookingStatus(Long bookingId, String trainerEmail, BookingStatus newStatus) {
        Booking booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new RuntimeException("Booking not found"));

        // Verify the trainer owns this booking
        if (!booking.getTrainer().getUser().getEmail().equals(trainerEmail)) {
            throw new RuntimeException("Unauthorized: Not your booking");
        }

        booking.setStatus(newStatus);
        return bookingRepository.save(booking);
    }

    /** User cancels their booking */
    public Booking cancelBooking(Long bookingId, String userEmail) {
        Booking booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (!booking.getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("Unauthorized: Not your booking");
        }

        booking.setStatus(BookingStatus.CANCELLED);
        return bookingRepository.save(booking);
    }

    private User getUser(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found: " + email));
    }
}
