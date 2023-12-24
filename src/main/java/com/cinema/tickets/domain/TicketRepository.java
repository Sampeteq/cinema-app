package com.cinema.tickets.domain;

import java.util.List;
import java.util.Optional;

public interface TicketRepository {
    Ticket add(Ticket ticket);
    Optional<Ticket> getByIdAndUserId(Long ticketId, Long userId);
    List<Ticket> getAllByUserId(Long userId);
    boolean existsBySeatId(Long seatId);
}
