package com.cinema.tickets.domain.repositories;

import com.cinema.tickets.domain.Ticket;

import java.util.List;
import java.util.Optional;

public interface TicketRepository {
    Ticket add(Ticket ticket);
    Optional<Ticket> getByIdAndUserId(Long ticketId, Long userId);
    List<Ticket> getAllByUserId(Long userId);
}
