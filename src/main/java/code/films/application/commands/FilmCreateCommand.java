package code.films.application.commands;

import code.films.domain.FilmCategory;
import lombok.Builder;
import lombok.With;

import javax.validation.constraints.NotNull;

@Builder
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
