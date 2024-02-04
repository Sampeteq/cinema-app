package com.cinema.tickets.ui;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record BookTicketDto(
        @NotNull
        Long screeningId,
        @NotNull
        List<Long> seatsIds
) {
}
