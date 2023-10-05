package com.cinema.catalog.application.dto;

import com.cinema.catalog.domain.FilmCategory;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record ScreeningQueryDto(
        LocalDate date,
        String filmTitle,
        FilmCategory filmCategory
) {
}
