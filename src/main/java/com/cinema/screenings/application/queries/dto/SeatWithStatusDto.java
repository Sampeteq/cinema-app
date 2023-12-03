package com.cinema.screenings.application.queries.dto;

public record SeatWithStatusDto(
        int rowNumber,
        int number,
        boolean isFree
) {
}
