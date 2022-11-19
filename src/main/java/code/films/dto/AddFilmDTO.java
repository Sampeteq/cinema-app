package code.films.dto;

import lombok.Builder;
import lombok.With;

import javax.validation.constraints.NotNull;

@Builder
@With
public record AddFilmDTO(
        @NotNull
        String title,

        @NotNull
        FilmCategoryDTO filmCategory,
        @NotNull
        Integer year
) {
}
