package com.cinema.catalog.application.dto;

import com.cinema.catalog.domain.FilmCategory;
import jakarta.validation.constraints.NotNull;
import lombok.With;

@With
public record FilmCreateDto(
        @NotNull
        String title,
        @NotNull
        FilmCategory category,
        @NotNull
        Integer year,
        @NotNull
        Integer durationInMinutes
) {
}
