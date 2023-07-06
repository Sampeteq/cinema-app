package code.catalog.application.dto;


import code.catalog.domain.FilmCategory;

public record FilmDto(
        Long id,
        String title,
        FilmCategory category,
        int year,
        int durationInMinutes
) {
}
