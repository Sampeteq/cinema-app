package code.films.domain.client.commands;

import code.films.domain.FilmCategory;
import lombok.Builder;
import lombok.With;

import javax.validation.constraints.NotNull;

@Builder
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
