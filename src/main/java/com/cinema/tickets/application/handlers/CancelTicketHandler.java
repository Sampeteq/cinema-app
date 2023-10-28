package com.cinema.tickets.application.handlers;

import com.cinema.screenings.application.handlers.ReadScreeningDateHandler;
import com.cinema.screenings.application.queries.ReadScreeningDate;
import com.cinema.shared.events.EventPublisher;
import com.cinema.tickets.application.commands.CancelTicket;
import com.cinema.tickets.domain.TicketRepository;
import com.cinema.tickets.domain.events.TicketCancelledEvent;
import com.cinema.tickets.domain.exceptions.TicketNotBelongsToUser;
import com.cinema.tickets.domain.exceptions.TicketNotFoundException;
import com.cinema.tickets.domain.policies.TicketCancellingPolicy;
import com.cinema.users.application.handlers.ReadCurrentUserIdHandler;
import com.cinema.users.application.queries.ReadCurrentUserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class CancelTicketHandler {

    private final TicketRepository ticketRepository;
    private final TicketCancellingPolicy ticketCancellingPolicy;
    private final ReadScreeningDateHandler readScreeningDateHandler;
    private final ReadCurrentUserIdHandler readCurrentUserIdHandler;
    private final EventPublisher eventPublisher;

    @Transactional
    public void handle(CancelTicket command) {
        log.info("Command id:{}", command);
        var ticket = ticketRepository
                .readById(command.ticketId())
                .orElseThrow(TicketNotFoundException::new);
        log.info("Found ticket:{}", ticket);
        var readCurrentUserIdQuery = new ReadCurrentUserId();
        var currentUserId = readCurrentUserIdHandler.handle(readCurrentUserIdQuery);
        if (!ticket.belongsTo(currentUserId)) {
            throw new TicketNotBelongsToUser();
        }
        var readScreeningDate = new ReadScreeningDate(ticket.getScreeningId());
        var screeningDate = readScreeningDateHandler.handle(readScreeningDate);
        log.info("Screening date:{}", screeningDate);
        ticketCancellingPolicy.checkScreeningDate(screeningDate);
        ticket.cancel();
        log.info("Ticket cancelled:{}", ticket);
        var ticketCancelledEvent = new TicketCancelledEvent(
                ticket.getScreeningId(),
                ticket.getSeatId()
        );
        eventPublisher.publish(ticketCancelledEvent);
        log.info("Event published:{}", ticketCancelledEvent);
    }
}
