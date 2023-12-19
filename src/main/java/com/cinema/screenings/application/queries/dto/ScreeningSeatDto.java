package com.cinema.screenings.application.queries.dto;

public record ScreeningSeatDto(
        long id,
        int rowNumber,
        int number,
        boolean isFree
) {
}
