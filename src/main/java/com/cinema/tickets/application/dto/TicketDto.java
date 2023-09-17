package com.cinema.tickets.application.dto;

import com.cinema.tickets.domain.TicketStatus;

import java.time.LocalDateTime;

public record TicketDto(
        Long id,
        TicketStatus status,
        String filmTitle,
        LocalDateTime screeningDate,
        String roomCustomId,
        Integer seatRowNumber,
        Integer seatNumber
) {
}
