package com.cinema.screenings.ui;

public record ScreeningSeatView(
        long id,
        int rowNumber,
        int number,
        boolean isFree
) {
}
