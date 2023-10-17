package com.cinema.tickets.domain;

import java.util.List;
import java.util.Optional;

public interface TicketRepository {
    Ticket add(Ticket ticket);
    Optional<Ticket> readById(Long ticketId);
    List<Ticket> readAllByUserId(Long userId);
    boolean exists(Long screeningId, Long seatId);
}
