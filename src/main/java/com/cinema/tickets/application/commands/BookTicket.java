package com.cinema.tickets.application.commands;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record BookTicket(
        @NotNull
        Long screeningId,
        @NotNull
        List<Long> seatsIds
) {
}
