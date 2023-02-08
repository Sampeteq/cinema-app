package code.films.application.dto;

import code.films.domain.FilmCategory;
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
