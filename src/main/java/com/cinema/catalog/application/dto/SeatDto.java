package com.cinema.catalog.application.dto;

public record SeatDto(
        int rowNumber,
        int number,
        boolean isFree
) {
}
