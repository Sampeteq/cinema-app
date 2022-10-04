package com.example.screening.domain.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ScreeningDTO(
        Long id,
        LocalDateTime date,
        int freeSeats,
        int minAge,
        Long filmId) {
}
