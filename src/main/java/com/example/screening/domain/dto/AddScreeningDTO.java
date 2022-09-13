package com.example.screening.domain.dto;

import com.example.screening.domain.ScreeningValues;
import lombok.Builder;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record AddScreeningDTO(
        UUID filmId,
        LocalDateTime date,
        @Positive
        int freeSeats,
        @Min(value = ScreeningValues.SCREENING_MIN_AGE)
        @Max(value = ScreeningValues.SCREENING_MAX_AGE)
        int minAge) {
}
