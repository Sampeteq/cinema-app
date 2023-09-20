package com.cinema.tickets.domain;

import com.cinema.tickets.domain.Ticket;

import java.util.List;
import java.util.Optional;

public interface TicketRepository {
    Ticket add(Ticket ticket);
    Optional<Ticket> readByIdAndUserId(Long ticketId, Long userId);
    List<Ticket> readAllByUserId(Long userId);
}
