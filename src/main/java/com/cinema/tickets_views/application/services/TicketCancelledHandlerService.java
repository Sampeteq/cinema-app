package com.cinema.tickets_views.application.services;

import com.cinema.tickets.domain.TicketStatus;
import com.cinema.tickets.domain.events.TicketCancelledEvent;
import com.cinema.tickets_views.domain.ports.TicketViewRepository;
import com.cinema.shared.exceptions.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
class TicketCancelledHandlerService {

    private final TicketViewRepository ticketViewRepository;

    @Transactional
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handle(TicketCancelledEvent event) {
        ticketViewRepository
                .readById(event.ticketId())
                .orElseThrow(() -> new EntityNotFoundException("Ticket view"))
                .changeStatus(TicketStatus.CANCELLED);
    }
}
