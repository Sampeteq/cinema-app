package com.cinema.tickets.application.commands.handlers;

import com.cinema.screenings.application.queries.handlers.ReadScreeningHandler;
import com.cinema.screenings.application.queries.ReadScreening;
import com.cinema.shared.events.EventPublisher;
import com.cinema.tickets.application.commands.CancelTicket;
import com.cinema.tickets.domain.TicketRepository;
import com.cinema.tickets.domain.events.TicketCancelledEvent;
import com.cinema.tickets.domain.exceptions.TicketNotBelongsToUserException;
import com.cinema.tickets.domain.exceptions.TicketNotFoundException;
import com.cinema.tickets.domain.policies.TicketCancellingPolicy;
import com.cinema.users.application.queries.handlers.ReadCurrentUserIdHandler;
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
    private final ReadScreeningHandler readScreeningHandler;
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
            throw new TicketNotBelongsToUserException();
        }
        var readScreening = new ReadScreening(ticket.getScreeningId());
        var screeningDto = readScreeningHandler.handle(readScreening);
        log.info("Screening:{}", screeningDto);
        ticketCancellingPolicy.checkScreeningDate(screeningDto.date());
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
