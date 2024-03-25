package com.cinema.tickets.domain;

import java.time.LocalDateTime;

public record TicketDto(
        Long id,
        String filmTitle,
        LocalDateTime screeningDate,
        Long hallId,
        Integer rowNumber,
        Integer seatNumber,
        Long userId
) {
}
