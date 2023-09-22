package com.cinema.tickets.application.services;

import com.cinema.shared.events.EventPublisher;
import com.cinema.shared.exceptions.EntityNotFoundException;
import com.cinema.shared.time.TimeProvider;
import com.cinema.tickets.domain.TicketRepository;
import com.cinema.tickets.domain.events.TicketCancelledEvent;
import com.cinema.users.application.services.UserFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
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
        var ticketCancelledEvent = new TicketCancelledEvent(
                ticket.getScreeningId(),
                ticket.getRowNumber(),
                ticket.getSeatNumber()
        );
        eventPublisher.publish(ticketCancelledEvent);
        log.info("Event published:{}", ticketCancelledEvent);
    }
}
