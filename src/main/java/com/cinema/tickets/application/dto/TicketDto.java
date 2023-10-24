package com.cinema.tickets.application.dto;

import com.cinema.tickets.domain.TicketStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record TicketDto(
        Long id,
        TicketStatus status,
        String filmTitle,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
        LocalDateTime screeningDate,
        String roomId,
        Integer rowNumber,
        Integer seatNumber
) {
}
