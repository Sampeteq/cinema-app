package com.cinema.tickets.application.commands.dto;

import jakarta.validation.constraints.NotNull;

public record SeatPositionDto(
        @NotNull
        Integer rowNumber,
        @NotNull
        Integer number
) {
}
