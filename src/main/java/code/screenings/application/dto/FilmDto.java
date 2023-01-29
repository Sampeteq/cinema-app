package code.screenings.application.dto;


import code.screenings.domain.FilmCategory;

import java.util.UUID;

public record FilmDto(
        UUID id,
        String title,
        FilmCategory category,
        int year,
        int durationInMinutes
) {
}
