package com.example.screening.domain.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record ScreeningDTO(
        UUID id,
        LocalDateTime date,
        int freeSeats,
        int minAge,
        UUID filmId) {
}
