package com.cinema.halls.application.queries.dto;

public record SeatWithStatusDto(
        int rowNumber,
        int number,
        boolean isFree
) {
}
