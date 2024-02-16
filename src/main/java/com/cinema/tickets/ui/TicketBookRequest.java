package com.cinema.tickets.ui;

import com.cinema.halls.domain.Seat;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record TicketBookRequest(
        @NotNull
        Long screeningId,
        @NotNull
        List<Seat> seats
) {
}
