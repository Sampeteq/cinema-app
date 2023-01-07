package code.screenings.dto;

import lombok.With;

import javax.validation.constraints.NotNull;

@With
public record CreateFilmDto(
        @NotNull
        String title,

        @NotNull
        FilmCategoryDto filmCategory,
        @NotNull
        Integer year,
        @NotNull
        Integer durationInMinutes
) {
}
