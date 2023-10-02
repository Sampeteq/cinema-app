package com.cinema.tickets.domain.events;

public record TicketBookedEvent(Long screeningId, int rowNumber, int seatNumber) {
}
