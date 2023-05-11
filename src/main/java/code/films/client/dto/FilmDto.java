package code.films.client.dto;


import code.films.domain.FilmCategory;

import java.util.UUID;

public record FilmDto(
        Long id,
        String title,
        FilmCategory category,
        int year,
        int durationInMinutes
) {
}
