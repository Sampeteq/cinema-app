package com.cinema.tickets.domain;

import com.cinema.halls.domain.HallService;
import com.cinema.halls.domain.Seat;
import com.cinema.screenings.domain.ScreeningService;
import com.cinema.tickets.domain.exceptions.TicketNotFoundException;
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
    private final HallService hallService;
    private final Clock clock;

    @Transactional
    public void addTickets(Long screeningId) {
        log.info("Screening id:{}", screeningId);
        var screening = screeningService.getScreeningById(screeningId);
        var tickets = hallService
                .getHallWithSeatsById(screening.getHallId())
                .getSeats()
                .stream()
                .map(seat -> new Ticket(screening.getId(), seat))
                .toList();
        tickets.forEach(ticketRepository::add);
    }

    @Transactional
    public void bookTickets(Long screeningId, List<Seat> seats, Long userId) {
        log.info("Screening id:{}", screeningId);
        log.info("Seats ids:{}", seats);
        var screening = screeningService.getScreeningById(screeningId);
        log.info("Found screening:{}", screening);
        ticketBookingPolicy.checkIfBookingIsPossible(screening.hoursLeftBeforeStart(clock));
        seats.forEach(
                seat -> {
                    var ticket = ticketRepository
                            .getBySeat(seat)
                            .orElseThrow(TicketNotFoundException::new);
                    log.info("Found ticket: {}", ticket);
                    ticket.assignUserId(userId);
                    log.info("Booked ticket:{}", ticket);
                }
        );
    }

    @Transactional
    public void cancelTicket(Long ticketId, Long userId) {
        log.info("Ticket id:{}", ticketId);
        var ticket = ticketRepository
                .getByIdAndUserId(ticketId, userId)
                .orElseThrow(TicketNotFoundException::new);
        log.info("Found ticket:{}", ticket);
        var screening = screeningService.getScreeningById(ticket.getScreeningId());
        ticketCancellingPolicy.checkIfCancellingIsPossible(screening.hoursLeftBeforeStart(clock));
        ticket.removeUserId();
        log.info("Ticket cancelled:{}", ticket);
    }

    public List<Ticket> getAllTicketsByUserId(Long userId) {
        return ticketRepository.getAllByUserId(userId);
    }

    public List<Ticket> getAllTicketsByScreeningId(Long screeningId) {
        return ticketRepository.getAllByScreeningId(screeningId);
    }
}
