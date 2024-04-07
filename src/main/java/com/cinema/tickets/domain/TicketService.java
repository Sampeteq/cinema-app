package com.cinema.tickets.domain;

import com.cinema.halls.domain.HallService;
import com.cinema.halls.domain.Seat;
import com.cinema.screenings.domain.ScreeningService;
import com.cinema.tickets.domain.exceptions.TicketBookTooLateException;
import com.cinema.tickets.domain.exceptions.TicketCancelTooLateException;
import com.cinema.tickets.domain.exceptions.TicketNotFoundException;
import com.cinema.users.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class TicketService {

    private final TicketRepository ticketRepository;
    private final TicketReadRepository ticketReadRepository;
    private final ScreeningService screeningService;
    private final HallService hallService;
    private final Clock clock;

    public void addTickets(Long screeningId) {
        log.info("Screening id:{}", screeningId);
        var screening = screeningService.getScreeningById(screeningId);
        hallService
                .getHallById(screening.getHallId())
                .getSeats()
                .stream()
                .map(seat -> new Ticket(null, screening, seat, null, 0))
                .forEach(ticketRepository::save);
    }

    @Transactional
    public void bookTickets(Long screeningId, List<Seat> seats, User user) {
        log.info("Screening id:{}", screeningId);
        log.info("Seats ids:{}", seats);
        var screening = screeningService.getScreeningById(screeningId);
        log.info("Found screening:{}", screening);
        if (screening.hoursLeftBeforeStart(clock) < 1) {
            throw new TicketBookTooLateException();
        }
        seats.forEach(
                seat -> {
                    var ticket = ticketRepository
                            .getByScreeningIdAndSeat(screeningId, seat)
                            .orElseThrow(TicketNotFoundException::new);
                    log.info("Found ticket: {}", ticket);
                    ticket.assignUser(user.getId());
                    ticketRepository.save(ticket);
                    log.info("Booked ticket:{}", ticket);
                }
        );
    }

    public void cancelTicket(Long ticketId, User user) {
        log.info("Ticket id:{}", ticketId);
        var ticket = ticketRepository
                .getByIdAndUserId(ticketId, user.getId())
                .orElseThrow(TicketNotFoundException::new);
        log.info("Found ticket:{}", ticket);
        var hoursLeftBeforeStart = ticket.getScreening().hoursLeftBeforeStart(clock);
        if (hoursLeftBeforeStart < 24) {
            throw new TicketCancelTooLateException();
        }
        ticket.removeUser();
        ticketRepository.save(ticket);
        log.info("Ticket cancelled:{}", ticket);
    }

    public List<TicketDto> getAllTicketsByUserId(Long userId) {
        return ticketReadRepository.getByUserId(userId);
    }

    public List<TicketDto> getAllTicketsByScreeningId(Long screeningId) {
        return ticketReadRepository.getByScreeningId(screeningId);
    }
}
