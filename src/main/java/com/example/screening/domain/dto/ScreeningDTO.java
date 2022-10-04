package com.example.screening.domain.dto;

import com.example.screening.domain.ScreeningId;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ScreeningDTO(
        ScreeningId id,
        LocalDateTime date,
        int freeSeats,
        int minAge,
        Long filmId) {
}
