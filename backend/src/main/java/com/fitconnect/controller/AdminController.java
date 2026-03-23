package com.fitconnect.controller;

import com.fitconnect.dto.ApiResponse;
import com.fitconnect.model.Booking;
import com.fitconnect.model.Trainer;
import com.fitconnect.model.User;
import com.fitconnect.repository.BookingRepository;
import com.fitconnect.repository.TrainerRepository;
import com.fitconnect.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired private UserRepository userRepository;
    @Autowired private TrainerRepository trainerRepository;
    @Autowired private BookingRepository bookingRepository;

    @GetMapping("/stats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", userRepository.count());
        stats.put("totalTrainers", trainerRepository.count());
        stats.put("totalBookings", bookingRepository.count());
        stats.put("pendingBookings", bookingRepository.findAll().stream()
            .filter(b -> b.getStatus() == Booking.BookingStatus.PENDING).count());
        return ResponseEntity.ok(ApiResponse.success("Stats fetched", stats));
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(ApiResponse.success("Users fetched", users));
    }

    @GetMapping("/trainers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllTrainers() {
        List<Trainer> trainers = trainerRepository.findAll();
        return ResponseEntity.ok(ApiResponse.success("Trainers fetched", trainers));
    }

    @GetMapping("/bookings")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllBookings() {
        List<Booking> bookings = bookingRepository.findAll();
        return ResponseEntity.ok(ApiResponse.success("Bookings fetched", bookings));
    }

    @DeleteMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return ResponseEntity.ok(ApiResponse.success("User deleted", null));
    }

    @DeleteMapping("/trainers/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteTrainer(@PathVariable Long id) {
        trainerRepository.deleteById(id);
        return ResponseEntity.ok(ApiResponse.success("Trainer deleted", null));
    }
}
