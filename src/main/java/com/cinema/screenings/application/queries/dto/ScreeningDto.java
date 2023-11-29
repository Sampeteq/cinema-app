package com.cinema.screenings.application.queries.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record ScreeningDto(
        Long id,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
        LocalDateTime date,
        String filmTitle,
        Long hallId
) {
}
