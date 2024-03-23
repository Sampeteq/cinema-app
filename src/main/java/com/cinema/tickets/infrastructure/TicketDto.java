package com.cinema.tickets.infrastructure;

import java.time.LocalDateTime;

record TicketDto(
        Long id,
        String filmTitle,
        LocalDateTime screeningDate,
        Long hallId,
        Integer rowNumber,
        Integer seatNumber,
        Long userId
) {
}
