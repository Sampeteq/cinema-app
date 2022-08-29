package com.example.screening.domain.dto;

import com.example.screening.domain.ScreeningValues;
import lombok.Builder;

import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record AddScreeningDTO(
        UUID filmId,
        LocalDateTime shownAt,
        @Positive
        int freeSeats,
        @Min(ScreeningValues.SCREENING_MIN_AGE)
        int minAge) {
}
