package com.cinema.tickets.application.queries.dto;

public record SeatDto(
        int rowNumber,
        int number,
        boolean isFree
) {
}
