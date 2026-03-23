package com.fitconnect.dto;

import lombok.Data;

/** DTO for creating/updating a Trainer profile */
@Data
public class TrainerDTO {
    private int experienceYears;
    private String skills;         // Comma-separated
    private String certifications;
    private Double hourlyRate;
    private String location;
    private String specialization;
    private Boolean isAvailable;
}
