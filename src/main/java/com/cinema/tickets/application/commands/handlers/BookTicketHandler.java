package com.cinema.tickets.application.commands.handlers;

import com.cinema.screenings.application.queries.handlers.GetScreeningHandler;
import com.cinema.screenings.application.queries.handlers.GetSeatHandler;
import com.cinema.screenings.application.queries.GetScreening;
import com.cinema.screenings.application.queries.GetSeat;
import com.cinema.shared.events.EventPublisher;
import com.cinema.tickets.application.commands.BookTicket;
import com.cinema.tickets.domain.Ticket;
import com.cinema.tickets.domain.TicketRepository;
import com.cinema.tickets.domain.TicketStatus;
import com.cinema.tickets.domain.events.TicketBookedEvent;
import com.cinema.tickets.domain.exceptions.TicketAlreadyExistsException;
import com.cinema.tickets.domain.policies.TicketBookingPolicy;
import com.cinema.users.application.queries.handlers.GetCurrentUserIdHandler;
import com.cinema.users.application.queries.GetCurrentUserId;
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
    private final GetScreeningHandler getScreeningHandler;
    private final GetSeatHandler getSeatHandler;
    private final GetCurrentUserIdHandler getCurrentUserIdHandler;
    private final EventPublisher eventPublisher;

    @Transactional
    public void handle(BookTicket command) {
        log.info("Command:{}", command);
        if (ticketRepository.exists(command.screeningId(), command.seatId())) {
            throw new TicketAlreadyExistsException();
        }
        var getScreening = new GetScreening(command.screeningId());
        var screeningDto = getScreeningHandler.handle(getScreening);
        log.info("Screening:{}", screeningDto);
        ticketBookingPolicy.checkScreeningDate(screeningDto.date());
        var getSeat = new GetSeat(command.screeningId(), command.seatId());
        var seatDto = getSeatHandler.handle(getSeat);
        var getCurrentUserIdCommand = new GetCurrentUserId();
        var currentUserId = getCurrentUserIdHandler.handle(getCurrentUserIdCommand);
        var ticket = new Ticket(
                TicketStatus.ACTIVE,
                command.screeningId(),
                seatDto.id(),
                currentUserId
        );
        var addedTicket = ticketRepository.add(ticket);
        log.info("Added ticket:{}", addedTicket);
        var ticketBookedEvent = new TicketBookedEvent(
                command.screeningId(),
                command.seatId()
        );
        eventPublisher.publish(ticketBookedEvent);
        log.info("Event published:{}", ticketBookedEvent);
    }
}
