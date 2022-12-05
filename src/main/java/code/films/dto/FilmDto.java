package code.films.dto;


import lombok.Builder;

import java.util.UUID;

@Builder
public record FilmDto(
        UUID id,
        String title,
        FilmCategoryDto category,
        int year
) {
}
