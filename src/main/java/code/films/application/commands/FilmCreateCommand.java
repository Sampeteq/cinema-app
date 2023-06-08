package code.films.application.commands;

import code.films.domain.FilmCategory;
import lombok.With;

import javax.validation.constraints.NotNull;

@With
public record FilmCreateCommand(
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
