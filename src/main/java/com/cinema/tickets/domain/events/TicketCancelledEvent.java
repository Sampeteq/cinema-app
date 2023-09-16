package com.cinema.tickets.domain.events;

import com.cinema.shared.events.Event;

public record TicketCancelledEvent(Long ticketId) implements Event {
}
