package code.films.application.dto;


import code.films.domain.FilmCategory;

public record FilmDto(
        Long id,
        String title,
        FilmCategory category,
        int year,
        int durationInMinutes
) {
}
