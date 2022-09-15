package com.example.screening.domain.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record AddScreeningDTO(
        UUID filmId,
        LocalDateTime date,
        int freeSeats,
        int minAge) {

}
