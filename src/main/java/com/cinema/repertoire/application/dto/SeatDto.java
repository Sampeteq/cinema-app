package com.cinema.repertoire.application.dto;

import com.cinema.repertoire.domain.SeatStatus;

public record SeatDto(
        int rowNumber,
        int number,
        SeatStatus status
) {
}
