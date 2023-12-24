package com.cinema.tickets.application.commands.handlers;

import com.cinema.screenings.application.queries.GetScreeningDate;
import com.cinema.screenings.application.queries.handlers.GetScreeningDateHandler;
import com.cinema.shared.events.EventPublisher;
import com.cinema.tickets.application.commands.CancelTicket;
import com.cinema.tickets.domain.TicketCancelled;
import com.cinema.tickets.domain.TicketCancellingPolicy;
import com.cinema.tickets.domain.TicketRepository;
import com.cinema.tickets.domain.exceptions.TicketNotFoundException;
import com.cinema.users.application.queries.GetCurrentUserId;
import com.cinema.users.application.queries.handlers.GetCurrentUserIdHandler;
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
    private final GetScreeningDateHandler getScreeningDateHandler;
    private final GetCurrentUserIdHandler getCurrentUserIdHandler;
    private final EventPublisher eventPublisher;

    @Transactional
    public void handle(CancelTicket command) {
        log.info("Command id:{}", command);
        var currentUserId = getCurrentUserIdHandler.handle(new GetCurrentUserId());
        var ticket = ticketRepository
                .getByIdAndUserId(command.ticketId(), currentUserId)
                .orElseThrow(TicketNotFoundException::new);
        log.info("Found ticket:{}", ticket);
        var screeningDate = getScreeningDateHandler.handle(new GetScreeningDate(ticket.getScreeningId()));
        log.info("Screening date:{}", screeningDate);
        ticketCancellingPolicy.checkScreeningDate(screeningDate);
        ticket.cancel();
        log.info("Ticket cancelled:{}", ticket);
        var event = new TicketCancelled(ticket.getSeatId());
        eventPublisher.publish(event);
        log.info("Published event:{}", event);
    }
}
