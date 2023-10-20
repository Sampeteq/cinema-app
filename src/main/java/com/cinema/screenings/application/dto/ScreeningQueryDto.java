package com.cinema.screenings.application.dto;

import com.cinema.films.domain.FilmCategory;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record ScreeningQueryDto(
        LocalDate date,
        String filmTitle,
        FilmCategory filmCategory
) {
}
