package com.cinema.screenings.application.queries.dto;

public record ScreeningSeatDto(
        int rowNumber,
        int number,
        boolean isFree
) {
}
