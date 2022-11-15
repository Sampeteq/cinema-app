package code.films.dto;

import code.films.FilmCategory;

import java.util.UUID;

public record FilmDTO(
        UUID id,
        String title,
        FilmCategory category,
        int year
) {
}
