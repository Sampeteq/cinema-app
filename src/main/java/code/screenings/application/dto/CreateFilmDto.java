package code.screenings.application.dto;

import code.screenings.domain.FilmCategory;
import lombok.With;

import javax.validation.constraints.NotNull;

@With
public record CreateFilmDto(
        @NotNull
        String title,
        @NotNull
        FilmCategory filmCategory,
        @NotNull
        Integer year,
        @NotNull
        Integer durationInMinutes
) {
}
