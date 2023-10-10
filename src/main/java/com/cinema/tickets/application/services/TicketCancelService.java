package com.cinema.tickets.application.services;

import com.cinema.shared.events.EventPublisher;
import com.cinema.shared.exceptions.EntityNotFoundException;
import com.cinema.tickets.domain.TicketRepository;
import com.cinema.tickets.domain.events.TicketCancelledEvent;
import com.cinema.tickets.domain.exceptions.TicketNotBelongsToUser;
import com.cinema.users.application.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;

@Service
@RequiredArgsConstructor
@Slf4j
class TicketCancelService {

    private final UserService userService;
    private final TicketRepository ticketRepository;
    private final Clock clock;
    private final EventPublisher eventPublisher;

    @Transactional
    public void cancelTicket(Long id) {
        var ticket = ticketRepository
                .readById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ticket"));
        var currentUserId = userService.readCurrentUserId();
        if (!ticket.belongsTo(currentUserId)) {
            throw new TicketNotBelongsToUser();
        }
        ticket.cancel(clock);
        var ticketCancelledEvent = new TicketCancelledEvent(
                ticket.getScreeningId(),
                ticket.getRowNumber(),
                ticket.getSeatNumber()
        );
        eventPublisher.publish(ticketCancelledEvent);
        log.info("Event published:{}", ticketCancelledEvent);
    }
}
