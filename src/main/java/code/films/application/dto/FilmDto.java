package code.films.application.dto;


import code.films.domain.FilmCategory;

import java.util.List;

public record FilmDto(
        Long id,
        String title,
        FilmCategory category,
        int year,
        int durationInMinutes,
        List<FilmScreeningDto> screenings
) {
}
