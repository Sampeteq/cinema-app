package code.screenings.dto;

import lombok.With;

import javax.validation.constraints.NotNull;

@With
public record FilmCreatingRequest(
        @NotNull
        String title,

        @NotNull
        FilmCategoryView filmCategory,
        @NotNull
        Integer year,
        @NotNull
        Integer durationInMinutes
) {
}
