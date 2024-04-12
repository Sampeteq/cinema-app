package com.cinema.tickets.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public record TicketUserDto(
        UUID id,
        String filmTitle,
        LocalDateTime screeningDate,
        UUID hallId,
        Integer rowNumber,
        Integer seatNumber,
        UUID userId
) {
}
