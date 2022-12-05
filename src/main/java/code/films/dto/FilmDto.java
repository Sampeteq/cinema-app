package code.films.dto;


import java.util.UUID;

public record FilmDto(
        UUID id,
        String title,
        String category,
        int year
) {
}
