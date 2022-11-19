package code.films.dto;


import java.util.UUID;

public record FilmDTO(
        UUID id,
        String title,
        String category,
        int year
) {
}
