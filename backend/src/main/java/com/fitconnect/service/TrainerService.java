package com.fitconnect.service;

import com.fitconnect.dto.TrainerDTO;
import com.fitconnect.model.Trainer;
import com.fitconnect.model.User;
import com.fitconnect.repository.TrainerRepository;
import com.fitconnect.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * TrainerService - Business logic for Trainer operations
 */
@Service
public class TrainerService {

    @Autowired private TrainerRepository trainerRepository;
    @Autowired private UserRepository userRepository;

    /** Get all trainers */
    public List<Trainer> getAllTrainers() {
        return trainerRepository.findAll();
    }

    /** Get trainer by ID */
    public Trainer getTrainerById(Long id) {
        return trainerRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Trainer not found with id: " + id));
    }

    /** Get trainer profile by user email */
    public Trainer getTrainerByEmail(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));
        return trainerRepository.findByUser(user)
            .orElseThrow(() -> new RuntimeException("Trainer profile not found"));
    }

    /** Create trainer profile for a USER with TRAINER role */
    public Trainer createTrainerProfile(String email, TrainerDTO dto) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRole() != User.Role.TRAINER) {
            throw new RuntimeException("Only TRAINER role users can create trainer profiles");
        }

        // Check if profile already exists
        if (trainerRepository.findByUser(user).isPresent()) {
            throw new RuntimeException("Trainer profile already exists");
        }

        Trainer trainer = mapDtoToTrainer(new Trainer(), dto);
        trainer.setUser(user);
        if (trainer.getLocation() == null) trainer.setLocation(user.getLocation());

        return trainerRepository.save(trainer);
    }

    /** Update trainer profile */
    public Trainer updateTrainerProfile(String email, TrainerDTO dto) {
        Trainer trainer = getTrainerByEmail(email);
        mapDtoToTrainer(trainer, dto);
        return trainerRepository.save(trainer);
    }

    /** Search trainers with filters */
    public List<Trainer> searchTrainers(String location, String skill,
                                         Double maxRate, Integer minExp) {
        return trainerRepository.searchTrainers(location, skill, maxRate, minExp);
    }

    /** Delete trainer profile */
    public void deleteTrainer(Long id) {
        trainerRepository.deleteById(id);
    }

    // Map DTO fields to Trainer entity
    private Trainer mapDtoToTrainer(Trainer trainer, TrainerDTO dto) {
        if (dto.getExperienceYears() > 0) trainer.setExperienceYears(dto.getExperienceYears());
        if (dto.getSkills() != null) trainer.setSkills(dto.getSkills());
        if (dto.getCertifications() != null) trainer.setCertifications(dto.getCertifications());
        if (dto.getHourlyRate() != null) trainer.setHourlyRate(dto.getHourlyRate());
        if (dto.getLocation() != null) trainer.setLocation(dto.getLocation());
        if (dto.getSpecialization() != null) trainer.setSpecialization(dto.getSpecialization());
        if (dto.getIsAvailable() != null) trainer.setIsAvailable(dto.getIsAvailable());
        return trainer;
    }
}
