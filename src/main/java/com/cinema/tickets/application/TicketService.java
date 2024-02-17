package com.cinema.tickets.application;

import com.cinema.halls.domain.Seat;
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
    public void addTickets(Long screeningId) {
        log.info("Screening id:{}", screeningId);
        var screening = screeningService.getScreeningById(screeningId);
        var tickets = screening
                .getHall()
                .getSeats()
                .stream()
                .map(seat -> new Ticket(screening, seat))
                .toList();
        ticketRepository.saveAll(tickets);
    }

    @Transactional
    public void bookTickets(Long screeningId, List<Seat> seats) {
        log.info("Screening id:{}", screeningId);
        log.info("Seats ids:{}", seats);
        var screening = screeningService.getScreeningById(screeningId);
        log.info("Found screening:{}", screening);
        var loggedUser = userService.getLoggedUserId();
        seats.forEach(
                seat -> {
                    var ticket = ticketRepository
                            .findBySeat(seat)
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
        var loggedUserId = userService.getLoggedUserId();
        var ticket = ticketRepository
                .findByIdAndUserId(ticketId, loggedUserId)
                .orElseThrow(TicketNotFoundException::new);
        log.info("Found ticket:{}", ticket);
        ticket.cancel(ticketCancellingPolicy, clock);
        log.info("Ticket cancelled:{}", ticket);
    }

    public List<Ticket> getAllTicketsByLoggedUser() {
        var loggedUser = userService.getLoggedUserId();
        return ticketRepository.findAllByUserId(loggedUser);
    }

    public List<Ticket> getAllTicketsByScreeningId(Long screeningId) {
        return ticketRepository.findAllByScreeningId(screeningId);
    }
}
