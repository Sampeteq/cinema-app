package code.films.dto;

import lombok.Builder;
import lombok.With;

import javax.validation.constraints.NotNull;

@Builder
@With
public record AddFilmDto(
        @NotNull
        String title,

        @NotNull
        FilmCategoryDto filmCategory,
        @NotNull
        Integer year
) {
}
