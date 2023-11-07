package com.cinema.tickets.domain;

import java.util.List;
import java.util.Optional;

public interface TicketRepository {
    Ticket add(Ticket ticket);
    Optional<Ticket> getById(Long ticketId);
    List<Ticket> getAllByUserId(Long userId);
    boolean exists(Long screeningId, Long seatId);
}
