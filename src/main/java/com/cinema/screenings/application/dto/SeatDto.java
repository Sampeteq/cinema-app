package com.cinema.screenings.application.dto;

import com.cinema.screenings.domain.SeatStatus;

public record SeatDto(
        Long id,
        int rowNumber,
        int number,
        SeatStatus status
) {
}
