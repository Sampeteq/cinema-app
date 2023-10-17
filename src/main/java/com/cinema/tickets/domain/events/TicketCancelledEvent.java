package com.cinema.tickets.domain.events;

public record TicketCancelledEvent(Long screeningId, Long seatId) {
}
