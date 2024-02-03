package com.cinema.tickets.application;

import com.cinema.screenings.domain.ScreeningRepository;
import com.cinema.screenings.domain.exceptions.ScreeningNotFoundException;
import com.cinema.tickets.application.dto.BookTicketDto;
import com.cinema.tickets.application.dto.TicketDto;
import com.cinema.tickets.domain.TicketBookingPolicy;
import com.cinema.tickets.domain.TicketCancellingPolicy;
import com.cinema.tickets.domain.TicketRepository;
import com.cinema.tickets.domain.exceptions.TicketNotFoundException;
import com.cinema.tickets.infrastructure.TicketMapper;
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
    private final TicketMapper ticketMapper;
    private final ScreeningRepository screeningRepository;
    private final UserService userService;
    private final Clock clock;

    @Transactional
    public void bookTicket(BookTicketDto dto) {
        log.info("Dto:{}", dto);
        var screening = screeningRepository
                .findById(dto.screeningId())
                .orElseThrow(ScreeningNotFoundException::new);
        log.info("Found screening:{}", screening);
        var loggedUser = userService.getLoggedUser();
        dto
                .seatsIds()
                .stream()
                .map(seatId -> {
                    var ticket = screening.findTicketBySeatId(seatId);
                    log.info("Found ticket: {}", ticket);
                    ticket.book(ticketBookingPolicy, clock, loggedUser);
                    return ticket;
                })
                .toList()
                .forEach(ticket -> {
                    var addedTicket = ticketRepository.save(ticket);
                    log.info("Added ticket:{}", addedTicket);
                });
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

    public List<TicketDto> getAllTicketsByLoggedUser() {
        var loggedUser = userService.getLoggedUser();
        return ticketRepository
                .findAllByUserId(loggedUser.getId())
                .stream()
                .map(ticketMapper::mapToDto)
                .toList();
    }
}
