package com.cinema.tickets.application.commands;

import jakarta.validation.constraints.NotNull;

public record BookTicket(
        @NotNull
        Long screeningId,

        @NotNull
        Integer rowNumber,

        @NotNull
        Integer seatNumber
) {
}
