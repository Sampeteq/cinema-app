package com.cinema.screenings.application.dto;

public record ScreeningSeatDto(
        long id,
        int rowNumber,
        int number,
        boolean isFree
) {
}
