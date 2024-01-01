package com.cinema.tickets.application.commands.handlers;

import com.cinema.screenings.application.queries.GetTimeToScreeningInHours;
import com.cinema.screenings.application.queries.handlers.GetTimeToScreeningInHoursHandler;
import com.cinema.shared.events.EventPublisher;
import com.cinema.tickets.application.commands.CancelTicket;
import com.cinema.tickets.domain.TicketCancelled;
import com.cinema.tickets.domain.TicketCancellingPolicy;
import com.cinema.tickets.domain.TicketRepository;
import com.cinema.tickets.domain.exceptions.TicketNotFoundException;
import com.cinema.users.application.queries.GetLoggedUserId;
import com.cinema.users.application.queries.handlers.GetLoggedUserIdHandler;
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
    private final GetTimeToScreeningInHoursHandler getTimeToScreeningInHoursHandler;
    private final GetLoggedUserIdHandler getLoggedUserIdHandler;
    private final EventPublisher eventPublisher;

    @Transactional
    public void handle(CancelTicket command) {
        log.info("Command id:{}", command);
        var loggedUserId = getLoggedUserIdHandler.handle(new GetLoggedUserId());
        var ticket = ticketRepository
                .getByIdAndUserId(command.ticketId(), loggedUserId)
                .orElseThrow(TicketNotFoundException::new);
        log.info("Found ticket:{}", ticket);
        var timeToScreeningInHours = getTimeToScreeningInHoursHandler.handle(new GetTimeToScreeningInHours(ticket.getScreeningId()));
        log.info("Time to screening in hours:{}", timeToScreeningInHours);
        ticketCancellingPolicy.checkScreeningDate(timeToScreeningInHours);
        ticket.cancel();
        log.info("Ticket cancelled:{}", ticket);
        var event = new TicketCancelled(ticket.getSeatId());
        eventPublisher.publish(event);
        log.info("Published event:{}", event);
    }
}
