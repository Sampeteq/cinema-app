package com.cinema.tickets.domain.events;

public record TicketBookedEvent(Long screeningId, Long seatId) {
}
