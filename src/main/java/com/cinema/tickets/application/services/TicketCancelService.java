package com.cinema.tickets.application.services;

import com.cinema.shared.events.EventPublisher;
import com.cinema.shared.exceptions.EntityNotFoundException;
import com.cinema.shared.time.TimeProvider;
import com.cinema.tickets.domain.events.TicketCancelledEvent;
import com.cinema.tickets.domain.ports.TicketRepository;
import com.cinema.user.application.services.UserFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
class TicketCancelService {

    private final UserFacade userFacade;
    private final TicketRepository ticketRepository;
    private final TimeProvider timeProvider;
    private final EventPublisher eventPublisher;

    @Transactional
    public void cancelTicket(Long ticketId) {
        var currentUserId = userFacade.readCurrentUserId();
        var ticket = ticketRepository
                .readByIdAndUserId(ticketId, currentUserId)
                .orElseThrow(() -> new EntityNotFoundException("Ticket"));
        ticket.cancel(timeProvider.getCurrentDate());
        var ticketCancelledEvent = new TicketCancelledEvent(ticketId);
        eventPublisher.publish(ticketCancelledEvent);
    }
}
