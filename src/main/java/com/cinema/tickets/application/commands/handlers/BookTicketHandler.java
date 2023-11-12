package com.cinema.tickets.application.commands.handlers;

import com.cinema.screenings.application.queries.GetScreening;
import com.cinema.screenings.application.queries.handlers.GetScreeningHandler;
import com.cinema.tickets.application.commands.BookTicket;
import com.cinema.tickets.domain.repositories.SeatRepository;
import com.cinema.tickets.domain.Ticket;
import com.cinema.tickets.domain.repositories.TicketRepository;
import com.cinema.tickets.domain.TicketStatus;
import com.cinema.tickets.domain.exceptions.SeatNotFoundException;
import com.cinema.tickets.domain.exceptions.TicketAlreadyExistsException;
import com.cinema.tickets.domain.policies.TicketBookingPolicy;
import com.cinema.users.application.queries.GetCurrentUserId;
import com.cinema.users.application.queries.handlers.GetCurrentUserIdHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class BookTicketHandler {

    private final TicketRepository ticketRepository;
    private final TicketBookingPolicy ticketBookingPolicy;
    private final SeatRepository seatRepository;
    private final GetScreeningHandler getScreeningHandler;
    private final GetCurrentUserIdHandler getCurrentUserIdHandler;

    @Transactional
    public void handle(BookTicket command) {
        log.info("Command:{}", command);
        if (ticketRepository.exists(command.seatId())) {
            throw new TicketAlreadyExistsException();
        }
        var screeningDto = getScreeningHandler.handle(new GetScreening(command.screeningId()));
        log.info("Screening:{}", screeningDto);
        ticketBookingPolicy.checkScreeningDate(screeningDto.date());
        var seat = seatRepository
                .getById(command.seatId())
                .orElseThrow(SeatNotFoundException::new);
        seat.take();
        var currentUserId = getCurrentUserIdHandler.handle(new GetCurrentUserId());
        var ticket = new Ticket(
                TicketStatus.ACTIVE,
                seat,
                currentUserId
        );
        var addedTicket = ticketRepository.add(ticket);
        log.info("Added ticket:{}", addedTicket);
    }
}
