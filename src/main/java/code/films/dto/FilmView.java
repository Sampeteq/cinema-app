package code.films.dto;


import java.util.UUID;

public record FilmView(
        UUID id,
        String title,
        FilmCategoryView category,
        int year,
        int durationInMinutes
) {
}
