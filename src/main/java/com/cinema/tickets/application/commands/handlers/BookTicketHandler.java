package com.cinema.tickets.application.commands.handlers;

import com.cinema.screenings.application.queries.GetSeatById;
import com.cinema.screenings.application.queries.handlers.GetSeatByIdHandler;
import com.cinema.screenings.application.queries.GetScreening;
import com.cinema.screenings.application.queries.handlers.GetScreeningHandler;
import com.cinema.shared.events.EventPublisher;
import com.cinema.tickets.application.commands.BookTicket;
import com.cinema.tickets.domain.Ticket;
import com.cinema.tickets.domain.TicketBooked;
import com.cinema.tickets.domain.TicketBookingPolicy;
import com.cinema.tickets.domain.TicketRepository;
import com.cinema.tickets.domain.TicketStatus;
import com.cinema.tickets.domain.exceptions.TicketAlreadyExistsException;
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
    private final GetScreeningHandler getScreeningHandler;
    private final GetSeatByIdHandler getSeatByIdHandler;
    private final GetCurrentUserIdHandler getCurrentUserIdHandler;
    private final EventPublisher eventPublisher;

    @Transactional
    public void handle(BookTicket command) {
        log.info("Command:{}", command);
        var screeningDto = getScreeningHandler.handle(new GetScreening(command.screeningId()));
        log.info("Screening:{}", screeningDto);
        ticketBookingPolicy.checkScreeningDate(screeningDto.date());
        var currentUserId = getCurrentUserIdHandler.handle(new GetCurrentUserId());
        command
                .seatsIds()
                .stream()
                .map(seatId -> {
                    var seatDto = getSeatByIdHandler.handle(new GetSeatById(seatId));
                    log.info("Found seat: {}", seatDto);
                    if (ticketRepository.existsBySeatId(seatDto.id())) {
                        throw new TicketAlreadyExistsException();
                    }
                    return new Ticket(TicketStatus.BOOKED, command.screeningId(), seatDto.id(), currentUserId);
                })
                .toList()
                .stream()
                .map(ticket -> {
                    var addedTicket = ticketRepository.add(ticket);
                    log.info("Added ticket:{}", addedTicket);
                    return addedTicket;
                })
                .toList()
                .forEach(addedTicket -> {
                    var event = new TicketBooked(addedTicket.getSeatId());
                    eventPublisher.publish(event);
                    log.info("Published event:{}", event);
                });
    }
}
