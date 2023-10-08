package com.cinema.repertoire.application.dto;

import com.cinema.repertoire.domain.FilmCategory;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record ScreeningQueryDto(
        LocalDate date,
        String filmTitle,
        FilmCategory filmCategory
) {
}
