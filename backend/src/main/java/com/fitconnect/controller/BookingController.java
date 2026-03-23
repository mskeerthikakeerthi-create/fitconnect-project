package com.fitconnect.controller;

import com.fitconnect.dto.*;
import com.fitconnect.model.Booking;
import com.fitconnect.model.Booking.BookingStatus;
import com.fitconnect.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

/**
 * BookingController - Session booking management
 * Base URL: /api/bookings
 */
@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "*")
public class BookingController {

    @Autowired private BookingService bookingService;

    /**
     * POST /api/bookings
     * User creates a booking request
     */
    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody BookingDTO dto, Principal principal) {
        try {
            Booking booking = bookingService.createBooking(principal.getName(), dto);
            return ResponseEntity.ok(ApiResponse.success("Booking request sent!", booking));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * GET /api/bookings/my-bookings
     * Get current user's bookings
     */
    @GetMapping("/my-bookings")
    public ResponseEntity<?> getMyBookings(Principal principal) {
        List<Booking> bookings = bookingService.getUserBookings(principal.getName());
        return ResponseEntity.ok(ApiResponse.success("Bookings fetched", bookings));
    }

    /**
     * GET /api/bookings/trainer-bookings
     * Get all bookings for a trainer
     */
    @GetMapping("/trainer-bookings")
    public ResponseEntity<?> getTrainerBookings(Principal principal) {
        try {
            List<Booking> bookings = bookingService.getTrainerBookings(principal.getName());
            return ResponseEntity.ok(ApiResponse.success("Bookings fetched", bookings));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * PUT /api/bookings/{id}/status
     * Trainer accepts or rejects: ?status=ACCEPTED or ?status=REJECTED
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long id,
                                           @RequestParam String status,
                                           Principal principal) {
        try {
            BookingStatus bookingStatus = BookingStatus.valueOf(status.toUpperCase());
            Booking booking = bookingService.updateBookingStatus(id, principal.getName(), bookingStatus);
            return ResponseEntity.ok(ApiResponse.success("Booking updated", booking));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * PUT /api/bookings/{id}/cancel
     * User cancels their booking
     */
    @PutMapping("/{id}/cancel")
    public ResponseEntity<?> cancelBooking(@PathVariable Long id, Principal principal) {
        try {
            Booking booking = bookingService.cancelBooking(id, principal.getName());
            return ResponseEntity.ok(ApiResponse.success("Booking cancelled", booking));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}
