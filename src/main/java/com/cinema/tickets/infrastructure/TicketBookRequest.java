package com.cinema.tickets.infrastructure;

import com.cinema.halls.domain.Seat;
import jakarta.validation.constraints.NotNull;

import java.util.List;

record TicketBookRequest(
        @NotNull
        Long screeningId,
        @NotNull
        List<Seat> seats
) {
}
