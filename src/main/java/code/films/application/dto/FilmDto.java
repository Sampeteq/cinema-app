package code.films.application.dto;


import code.films.domain.FilmCategory;

import java.util.UUID;

public record FilmDto(
        UUID id,
        String title,
        FilmCategory category,
        int year,
        int durationInMinutes
) {
}
