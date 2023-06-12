package code.catalog.application.dto;


import code.catalog.domain.FilmCategory;

import java.util.List;

public record FilmDto(
        Long id,
        String title,
        FilmCategory category,
        int year,
        int durationInMinutes,
        List<ScreeningDto> screenings
) {
}
