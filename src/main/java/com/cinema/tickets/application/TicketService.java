package com.cinema.tickets.application;

import com.cinema.halls.application.HallService;
import com.cinema.halls.domain.Seat;
import com.cinema.screenings.application.ScreeningService;
import com.cinema.tickets.application.dto.TicketDto;
import com.cinema.tickets.application.dto.TicketUserDto;
import com.cinema.tickets.domain.Ticket;
import com.cinema.tickets.domain.TicketReadRepository;
import com.cinema.tickets.domain.TicketRepository;
import com.cinema.tickets.domain.exceptions.TicketBookTooLateException;
import com.cinema.tickets.domain.exceptions.TicketCancelTooLateException;
import com.cinema.tickets.domain.exceptions.TicketNotFoundException;
import com.cinema.users.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
public class TicketService {

    private final TicketRepository ticketRepository;
    private final TicketReadRepository ticketReadRepository;
    private final ScreeningService screeningService;
    private final HallService hallService;
    private final Clock clock;

    @Transactional
    public void addTickets(UUID screeningId) {
        log.info("Screening id:{}", screeningId);
        var screening = screeningService.getScreeningById(screeningId);
        hallService
                .getHallById(screening.getHallId())
                .getSeats()
                .stream()
                .map(seat -> new Ticket(UUID.randomUUID(), screening, seat, null, 0))
                .forEach(ticketRepository::save);
    }

    @Transactional
    public void bookTickets(UUID screeningId, List<Seat> seats, User user) {
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

    @Transactional
    public void cancelTicket(UUID ticketId, User user) {
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

    public List<TicketUserDto> getAllTicketsByUserId(UUID userId) {
        return ticketReadRepository.getByUserId(userId);
    }

    public List<TicketDto> getAllTicketsByScreeningId(UUID screeningId) {
        return ticketReadRepository.getByScreeningId(screeningId);
    }
}
