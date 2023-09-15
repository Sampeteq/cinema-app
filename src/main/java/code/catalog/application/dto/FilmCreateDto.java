package code.catalog.application.dto;

import code.catalog.domain.FilmCategory;
import lombok.With;

import jakarta.validation.constraints.NotNull;

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
