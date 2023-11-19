package com.cinema.tickets.application.commands;

import com.cinema.tickets.application.commands.dto.SeatPositionDto;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record BookTicket(
        @NotNull
        Long screeningId,

        @NotNull
        List<SeatPositionDto> seats
) {
}
