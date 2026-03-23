package com.fitconnect.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

/** DTO for submitting a review */
@Data
public class ReviewDTO {
    private Long trainerId;

    @Min(1) @Max(5)
    private Integer rating;

    private String comment;
}
