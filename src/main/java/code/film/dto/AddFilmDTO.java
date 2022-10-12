package code.film.dto;

import code.film.FilmCategory;
import lombok.Builder;

import javax.validation.constraints.NotNull;

@Builder
public record AddFilmDTO(@NotNull String title,
                         @NotNull FilmCategory filmCategory,
                         @NotNull Integer year) {
}
