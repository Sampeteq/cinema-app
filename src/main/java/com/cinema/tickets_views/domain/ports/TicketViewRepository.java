package com.cinema.tickets_views.domain.ports;

import com.cinema.tickets_views.domain.TicketView;

import java.util.List;
import java.util.Optional;

public interface TicketViewRepository {
    TicketView add(TicketView ticketView);
    Optional<TicketView> readById(Long id);
    List<TicketView> readAllByUserId(Long userId);
}
