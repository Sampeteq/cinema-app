package com.cinema.tickets.application.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record TicketBookDto(
        @NotNull
        Long screeningId,

        @NotNull
        @Positive
        Integer rowNumber,

        @NotNull
        @Positive
        Integer seatNumber
) {
}
