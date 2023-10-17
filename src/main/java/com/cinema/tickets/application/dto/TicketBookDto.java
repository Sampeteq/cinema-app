package com.cinema.tickets.application.dto;

import jakarta.validation.constraints.NotNull;

public record TicketBookDto(
        @NotNull
        Long screeningId,

        @NotNull
        Long seatId
) {
}
