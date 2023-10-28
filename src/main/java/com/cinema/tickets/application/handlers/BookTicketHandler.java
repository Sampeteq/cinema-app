package com.cinema.tickets.application.handlers;

import com.cinema.screenings.application.handlers.ReadScreeningDateHandler;
import com.cinema.screenings.application.handlers.SeatExistsHandler;
import com.cinema.screenings.application.queries.ReadScreeningDate;
import com.cinema.screenings.application.queries.SeatExists;
import com.cinema.screenings.domain.exceptions.SeatNotFoundException;
import com.cinema.shared.events.EventPublisher;
import com.cinema.tickets.application.commands.BookTicket;
import com.cinema.tickets.domain.Ticket;
import com.cinema.tickets.domain.TicketRepository;
import com.cinema.tickets.domain.TicketStatus;
import com.cinema.tickets.domain.events.TicketBookedEvent;
import com.cinema.tickets.domain.exceptions.TicketAlreadyExists;
import com.cinema.tickets.domain.policies.TicketBookingPolicy;
import com.cinema.users.application.handlers.ReadCurrentUserIdHandler;
import com.cinema.users.application.queries.ReadCurrentUserId;
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
    private final ReadScreeningDateHandler readScreeningDateHandler;
    private final SeatExistsHandler seatExistsHandler;
    private final ReadCurrentUserIdHandler readCurrentUserIdHandler;
    private final EventPublisher eventPublisher;

    @Transactional
    public void handle(BookTicket command) {
        log.info("Command:{}", command);
        if (ticketRepository.exists(command.screeningId(), command.seatId())) {
            throw new TicketAlreadyExists();
        }
        var readScreeningDate = new ReadScreeningDate(command.screeningId());
        var screeningDate = readScreeningDateHandler.handle(readScreeningDate);
        log.info("Screening date:{}", screeningDate);
        ticketBookingPolicy.checkScreeningDate(screeningDate);
        var seatExistsQuery = new SeatExists(command.screeningId(), command.seatId());
        var seatExists = seatExistsHandler.handle(seatExistsQuery);
        if (!seatExists) {
            throw new SeatNotFoundException();
        }
        var readCurrentUserIdCommand = new ReadCurrentUserId();
        var currentUserId = readCurrentUserIdHandler.handle(readCurrentUserIdCommand);
        var ticket = new Ticket(
                TicketStatus.ACTIVE,
                command.screeningId(),
                command.seatId(),
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
