package com.cinema.tickets.ui;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record TicketBookRequest(
        @NotNull
        Long screeningId,
        @NotNull
        List<Long> seatsIds
) {
}
