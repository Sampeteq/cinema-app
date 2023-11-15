package com.cinema.tickets.application.queries.dto;

import com.cinema.tickets.domain.SeatStatus;

public record SeatDto(
        int rowNumber,
        int number,
        SeatStatus status
) {
}
