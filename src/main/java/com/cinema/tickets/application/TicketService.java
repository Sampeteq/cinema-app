package com.cinema.tickets.application;

import com.cinema.screenings.application.ScreeningService;
import com.cinema.tickets.domain.Ticket;
import com.cinema.tickets.domain.TicketBookingPolicy;
import com.cinema.tickets.domain.TicketCancellingPolicy;
import com.cinema.tickets.domain.TicketRepository;
import com.cinema.tickets.domain.exceptions.TicketNotFoundException;
import com.cinema.users.application.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TicketService {

    private final TicketRepository ticketRepository;
    private final TicketBookingPolicy ticketBookingPolicy;
    private final TicketCancellingPolicy ticketCancellingPolicy;
    private final ScreeningService screeningService;
    private final UserService userService;
    private final Clock clock;

    @Transactional
    public void bookTickets(Long screeningId, List<Long> seatsIds) {
        log.info("Screening id:{}", screeningId);
        log.info("Seats ids:{}", seatsIds);
        var screening = screeningService.getScreeningById(screeningId);
        log.info("Found screening:{}", screening);
        var loggedUser = userService.getLoggedUser();
        seatsIds.forEach(
                seatId -> {
                    var ticket = ticketRepository
                            .findBySeatId(seatId)
                            .orElseThrow(TicketNotFoundException::new);
                    log.info("Found ticket: {}", ticket);
                    ticket.book(ticketBookingPolicy, clock, loggedUser);
                    log.info("Booked ticket:{}", ticket);
                }
        );
    }

    @Transactional
    public void cancelTicket(Long ticketId) {
        log.info("Ticket id:{}", ticketId);
        var loggedUser = userService.getLoggedUser();
        var ticket = ticketRepository
                .findByIdAndUserId(ticketId, loggedUser.getId())
                .orElseThrow(TicketNotFoundException::new);
        log.info("Found ticket:{}", ticket);
        ticket.cancel(ticketCancellingPolicy, clock);
        log.info("Ticket cancelled:{}", ticket);
    }

    public List<Ticket> getAllTicketsByLoggedUser() {
        var loggedUser = userService.getLoggedUser();
        return ticketRepository.findAllByUserId(loggedUser.getId());
    }

    public List<Ticket> getAllTicketsByScreeningId(Long screeningId) {
        return ticketRepository.findAllByScreeningId(screeningId);
    }
}
