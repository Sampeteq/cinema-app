package com.cinema.tickets.application.commands.handlers;

import com.cinema.screenings.application.ScreeningApi;
import com.cinema.tickets.application.commands.BookTicket;
import com.cinema.tickets.domain.Ticket;
import com.cinema.tickets.domain.TicketStatus;
import com.cinema.tickets.domain.exceptions.SeatNotFoundException;
import com.cinema.tickets.domain.policies.TicketBookingPolicy;
import com.cinema.tickets.domain.repositories.SeatRepository;
import com.cinema.tickets.domain.repositories.TicketRepository;
import com.cinema.users.application.UserApi;
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
    private final ScreeningApi screeningApi;
    private final UserApi userApi;

    @Transactional
    public void handle(BookTicket command) {
        log.info("Command:{}", command);
        var screeningDate = screeningApi.getScreeningDate(command.screeningId());
        log.info("Screening date:{}", screeningDate);
        ticketBookingPolicy.checkScreeningDate(screeningDate);
        var seat = seatRepository
                .getByScreeningIdRowNumberAndNumber(
                        command.screeningId(),
                        command.rowNumber(),
                        command.seatNumber()
                )
                .orElseThrow(SeatNotFoundException::new);
        seat.take();
        var currentUserId = userApi.getCurrentUserId();
        var ticket = new Ticket(
                TicketStatus.BOOKED,
                seat,
                currentUserId
        );
        var addedTicket = ticketRepository.add(ticket);
        log.info("Added ticket:{}", addedTicket);
    }
}
