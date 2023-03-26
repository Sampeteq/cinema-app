package code.bookings.application.dto;


import code.bookings.domain.FilmCategory;

import java.util.UUID;

public record FilmDto(
        UUID id,
        String title,
        FilmCategory category,
        int year,
        int durationInMinutes
) {
}
