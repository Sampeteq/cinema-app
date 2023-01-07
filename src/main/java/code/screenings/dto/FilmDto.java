package code.screenings.dto;


import java.util.UUID;

public record FilmDto(
        UUID id,
        String title,
        FilmCategoryDto category,
        int year,
        int durationInMinutes
) {
}