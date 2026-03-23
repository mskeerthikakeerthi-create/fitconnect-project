package com.fitconnect.controller;

import com.fitconnect.dto.*;
import com.fitconnect.model.Trainer;
import com.fitconnect.service.TrainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

/**
 * TrainerController - CRUD and search for Trainer profiles
 * Base URL: /api/trainers
 */
@RestController
@RequestMapping("/api/trainers")
@CrossOrigin(origins = "*")
public class TrainerController {

    @Autowired private TrainerService trainerService;

    /**
     * GET /api/trainers
     * Get all trainers (public)
     */
    @GetMapping
    public ResponseEntity<?> getAllTrainers() {
        List<Trainer> trainers = trainerService.getAllTrainers();
        return ResponseEntity.ok(ApiResponse.success("Trainers fetched", trainers));
    }

    /**
     * GET /api/trainers/{id}
     * Get a specific trainer (public)
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getTrainerById(@PathVariable Long id) {
        try {
            Trainer trainer = trainerService.getTrainerById(id);
            return ResponseEntity.ok(ApiResponse.success("Trainer found", trainer));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * GET /api/trainers/search?location=Mumbai&skill=Yoga&maxRate=100&minExp=2
     * Search trainers with filters (public)
     */
    @GetMapping("/search")
    public ResponseEntity<?> searchTrainers(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String skill,
            @RequestParam(required = false) Double maxRate,
            @RequestParam(required = false) Integer minExp) {
        List<Trainer> trainers = trainerService.searchTrainers(location, skill, maxRate, minExp);
        return ResponseEntity.ok(ApiResponse.success("Search results", trainers));
    }

    /**
     * POST /api/trainers/profile
     * Create trainer profile (TRAINER only)
     */
    @PostMapping("/profile")
    @PreAuthorize("hasRole('TRAINER')")
    public ResponseEntity<?> createProfile(@RequestBody TrainerDTO dto, Principal principal) {
        try {
            Trainer trainer = trainerService.createTrainerProfile(principal.getName(), dto);
            return ResponseEntity.ok(ApiResponse.success("Trainer profile created", trainer));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * PUT /api/trainers/profile
     * Update trainer profile (TRAINER only)
     */
    @PutMapping("/profile")
    @PreAuthorize("hasRole('TRAINER')")
    public ResponseEntity<?> updateProfile(@RequestBody TrainerDTO dto, Principal principal) {
        try {
            Trainer trainer = trainerService.updateTrainerProfile(principal.getName(), dto);
            return ResponseEntity.ok(ApiResponse.success("Profile updated", trainer));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * GET /api/trainers/my-profile
     * Get the logged-in trainer's own profile
     */
    @GetMapping("/my-profile")
    @PreAuthorize("hasRole('TRAINER')")
    public ResponseEntity<?> getMyProfile(Principal principal) {
        try {
            Trainer trainer = trainerService.getTrainerByEmail(principal.getName());
            return ResponseEntity.ok(ApiResponse.success("Profile fetched", trainer));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * DELETE /api/trainers/{id}
     * Delete trainer (admin/owner)
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('TRAINER')")
    public ResponseEntity<?> deleteTrainer(@PathVariable Long id) {
        trainerService.deleteTrainer(id);
        return ResponseEntity.ok(ApiResponse.success("Trainer deleted", null));
    }
}
