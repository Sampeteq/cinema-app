package com.cinema.tickets.infrastructure.ui;

import com.cinema.halls.domain.Seat;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record TicketBookDto(
        UUID screeningId,
        @NotNull
        List<Seat> seats
) {
}
