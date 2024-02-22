package com.cinema.tickets;

import com.cinema.halls.Seat;
import jakarta.validation.constraints.NotNull;

import java.util.List;

record TicketBookRequest(
        @NotNull
        Long screeningId,
        @NotNull
        List<Seat> seats
) {
}
