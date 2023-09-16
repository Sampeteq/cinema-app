package com.cinema.tickets.domain.events;

import com.cinema.shared.events.Event;

import java.time.LocalDateTime;

public record TicketBookedEvent(
        Long ticketId,
        Long screeningId,
        LocalDateTime screeningDate,
        Integer seatRowNumber,
        Integer seatNumber,
        Long userId
) implements Event {
}
