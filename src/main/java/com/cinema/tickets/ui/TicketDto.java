package com.cinema.tickets.ui;

import com.cinema.tickets.domain.Ticket;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record TicketDto(
        Long id,
        Ticket.Status status,
        String filmTitle,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
        LocalDateTime screeningDate,
        Long hallId,
        Integer rowNumber,
        Integer seatNumber
) {
}
