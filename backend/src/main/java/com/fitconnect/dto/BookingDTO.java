package com.fitconnect.dto;

import lombok.Data;
import java.time.LocalDate;

/** DTO for creating a booking */
@Data
public class BookingDTO {
    private Long trainerId;
    private LocalDate sessionDate;
    private String sessionTime;
    private String sessionType; // "Online" or "In-Person"
    private String notes;
}
