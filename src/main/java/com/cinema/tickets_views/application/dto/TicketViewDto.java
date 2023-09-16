package com.cinema.tickets_views.application.dto;

import com.cinema.tickets.domain.TicketStatus;

import java.time.LocalDateTime;

public record TicketViewDto(
        Long id,
        TicketStatus status,
        String filmTitle,
        LocalDateTime screeningDate,
        String roomCustomId,
        Integer seatRowNumber,
        Integer seatNumber
) {
}
