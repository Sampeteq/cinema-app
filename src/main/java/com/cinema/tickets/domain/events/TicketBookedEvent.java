package com.cinema.tickets.domain.events;

import com.cinema.shared.events.Event;

public record TicketBookedEvent(Long screeningId, int rowNumber, int seatNumber) implements Event {
}
