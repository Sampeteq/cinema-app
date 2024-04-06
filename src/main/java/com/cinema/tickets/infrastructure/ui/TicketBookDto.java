package com.cinema.tickets.infrastructure.ui;

import com.cinema.halls.domain.Seat;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record TicketBookDto(
        @NotNull
        Long screeningId,
        @NotNull
        List<Seat> seats
) {
}