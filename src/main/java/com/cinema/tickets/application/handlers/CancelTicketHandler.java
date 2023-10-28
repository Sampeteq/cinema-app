package com.cinema.tickets.application.handlers;

import com.cinema.screenings.application.services.ScreeningService;
import com.cinema.shared.events.EventPublisher;
import com.cinema.tickets.application.commands.CancelTicket;
import com.cinema.tickets.domain.TicketRepository;
import com.cinema.tickets.domain.events.TicketCancelledEvent;
import com.cinema.tickets.domain.exceptions.TicketNotBelongsToUser;
import com.cinema.tickets.domain.exceptions.TicketNotFoundException;
import com.cinema.tickets.domain.policies.TicketCancellingPolicy;
import com.cinema.users.application.services.UserService;
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
    private final ScreeningService screeningService;
    private final UserService userService;
    private final EventPublisher eventPublisher;

    @Transactional
    public void handle(CancelTicket command) {
        log.info("Command id:{}", command);
        var ticket = ticketRepository
                .readById(command.ticketId())
                .orElseThrow(TicketNotFoundException::new);
        log.info("Found ticket:{}", ticket);
        var currentUserId = userService.readCurrentUserId();
        if (!ticket.belongsTo(currentUserId)) {
            throw new TicketNotBelongsToUser();
        }
        var screeningDate = screeningService.readScreeningDate(ticket.getScreeningId());
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
