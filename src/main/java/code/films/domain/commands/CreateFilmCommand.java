package code.films.domain.commands;

import code.films.domain.FilmCategory;
import lombok.With;

import javax.validation.constraints.NotNull;

@With
public record CreateFilmCommand(
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
