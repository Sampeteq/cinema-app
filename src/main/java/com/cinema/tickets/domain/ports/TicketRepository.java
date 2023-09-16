package com.cinema.tickets.domain.ports;

import com.cinema.tickets.domain.Ticket;

import java.util.Optional;

public interface TicketRepository {
    Ticket add(Ticket ticket);
    Optional<Ticket> readByIdAndUserId(Long ticketId, Long userId);
}
