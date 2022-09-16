package com.example.screening.domain.dto;

import com.example.film.domain.FilmId;
import com.example.screening.domain.ScreeningId;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ScreeningDTO(
        ScreeningId id,
        LocalDateTime date,
        int freeSeats,
        int minAge,
        FilmId filmId) {
}
