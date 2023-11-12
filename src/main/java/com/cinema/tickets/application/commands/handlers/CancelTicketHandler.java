package com.cinema.tickets.application.commands.handlers;

import com.cinema.screenings.application.queries.GetScreening;
import com.cinema.screenings.application.queries.handlers.GetScreeningHandler;
import com.cinema.tickets.application.commands.CancelTicket;
import com.cinema.tickets.domain.repositories.TicketRepository;
import com.cinema.tickets.domain.exceptions.TicketNotFoundException;
import com.cinema.tickets.domain.policies.TicketCancellingPolicy;
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
    private final GetScreeningHandler getScreeningHandler;
    private final GetCurrentUserIdHandler getCurrentUserIdHandler;

    @Transactional
    public void handle(CancelTicket command) {
        log.info("Command id:{}", command);
        var currentUserId = getCurrentUserIdHandler.handle(new GetCurrentUserId());
        var ticket = ticketRepository
                .getByIdAndUserId(command.ticketId(), currentUserId)
                .orElseThrow(TicketNotFoundException::new);
        log.info("Found ticket:{}", ticket);
        var screeningDto = getScreeningHandler.handle(new GetScreening(ticket.getSeat().getScreeningId()));
        log.info("Screening:{}", screeningDto);
        ticketCancellingPolicy.checkScreeningDate(screeningDto.date());
        ticket.cancel();
        log.info("Ticket cancelled:{}", ticket);
    }
}
