package code.films.application.dto;

import lombok.With;

@With
public record FilmScreeningSeatDto(
        Long id,
        int rowNumber,
        int number,
        boolean isFree
) {
}
