package com.cinema.screenings.application.dto;

public record ScreeningSeatView(
        long id,
        int rowNumber,
        int number,
        boolean isFree
) {
}
