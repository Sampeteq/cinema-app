package com.cinema.tickets.application.commands.handlers;

import com.cinema.halls.application.queries.GetSeatBySeatId;
import com.cinema.halls.application.queries.handlers.GetSeatByIdHandler;
import com.cinema.screenings.application.queries.GetScreening;
import com.cinema.screenings.application.queries.handlers.GetScreeningHandler;
import com.cinema.tickets.application.commands.BookTicket;
import com.cinema.tickets.domain.Ticket;
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

    @Transactional
    public void handle(BookTicket command) {
        log.info("Command:{}", command);
        var screeningDto = getScreeningHandler.handle(new GetScreening(command.screeningId()));
        log.info("Screening:{}", screeningDto);
        ticketBookingPolicy.checkScreeningDate(screeningDto.date());
        var currentUserId = getCurrentUserIdHandler.handle(new GetCurrentUserId());
        command
                .seatsIds()
                .forEach(seatId -> {
                    var seatDto = getSeatByIdHandler.handle(new GetSeatBySeatId(seatId));
                    log.error("Found seat: {}", seatDto);
                    if (ticketRepository.existsBySeatId(seatDto.seatId())) {
                        throw new TicketAlreadyExistsException();
                    }
                    var addedTicket = ticketRepository.add(
                            new Ticket(TicketStatus.BOOKED, command.screeningId(), seatDto.seatId(), currentUserId)
                    );
                    log.info("Added ticket:{}", addedTicket);
                });
    }
}
