package code.films.dto;

import code.films.FilmCategory;
import lombok.Builder;
import lombok.With;

import javax.validation.constraints.NotNull;

@Builder
@With
public record AddFilmDTO(
        @NotNull
        String title,

        @NotNull
        FilmCategory filmCategory,
        @NotNull
        Integer year
) {
}
