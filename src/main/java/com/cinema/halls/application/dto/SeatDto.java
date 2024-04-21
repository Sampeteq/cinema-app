package com.cinema.halls.application.dto;

import jakarta.validation.constraints.Positive;

record SeatDto(
        @Positive
        int rowNumber,
        @Positive
        int number
) {
}
