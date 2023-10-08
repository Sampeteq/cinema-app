package com.cinema.repertoire.application.dto;

import com.cinema.repertoire.domain.FilmCategory;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record ScreeningDto(
        Long id,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
        LocalDateTime date,
        String filmTitle,
        FilmCategory filmCategory
) {
}
