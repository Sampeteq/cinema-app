package com.cinema.tickets.application.services;

import com.cinema.repertoire.application.services.ScreeningService;
import com.cinema.shared.events.EventPublisher;
import com.cinema.shared.exceptions.EntityNotFoundException;
import com.cinema.tickets.application.dto.TicketBookDto;
import com.cinema.tickets.application.dto.TicketDto;
import com.cinema.tickets.domain.Ticket;
import com.cinema.tickets.domain.TicketRepository;
import com.cinema.tickets.domain.TicketStatus;
import com.cinema.tickets.domain.events.TicketBookedEvent;
import com.cinema.tickets.domain.events.TicketCancelledEvent;
import com.cinema.tickets.domain.exceptions.TicketAlreadyExists;
import com.cinema.tickets.domain.exceptions.TicketBookTooLateException;
import com.cinema.tickets.domain.exceptions.TicketCancelTooLateException;
import com.cinema.tickets.domain.exceptions.TicketNotBelongsToUser;
import com.cinema.users.application.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TicketService {

    private final TicketRepository ticketRepository;
    private final ScreeningService screeningService;
    private final UserService userService;
    private final Clock clock;
    private final EventPublisher eventPublisher;

    @Transactional
    public void bookTicket(TicketBookDto dto) {
        if (ticketRepository.exists(dto.screeningId(), dto.seatId())) {
            throw new TicketAlreadyExists();
        }
        var screeningDate = screeningService.readScreeningDate(dto.screeningId());
        if (timeToScreeningInHours(clock, screeningDate) < 1) {
            throw new TicketBookTooLateException();
        }
        var seatExists = screeningService.seatExists(dto.screeningId(), dto.seatId());
        if (!seatExists) {
            throw new EntityNotFoundException("Seat");
        }
        var currentUserId = userService.readCurrentUserId();
        var ticket = new Ticket(
                TicketStatus.ACTIVE,
                dto.screeningId(),
                dto.seatId(),
                currentUserId
        );
        var addedTicket = ticketRepository.add(ticket);
        log.info("Added a ticket:{}", addedTicket);
        var ticketBookedEvent = new TicketBookedEvent(
                dto.screeningId(),
                dto.seatId()
        );
        eventPublisher.publish(ticketBookedEvent);
        log.info("Event published:{}", ticketBookedEvent);
    }

    @Transactional
    public void cancelTicket(Long id) {
        var ticket = ticketRepository
                .readById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ticket"));
        var currentUserId = userService.readCurrentUserId();
        if (!ticket.belongsTo(currentUserId)) {
            throw new TicketNotBelongsToUser();
        }
        var screeningDate = screeningService.readScreeningDate(ticket.getScreeningId());
        if (timeToScreeningInHours(clock, screeningDate) < 24) {
            throw new TicketCancelTooLateException();
        }
        ticket.cancel();
        var ticketCancelledEvent = new TicketCancelledEvent(
                ticket.getScreeningId(),
                ticket.getSeatId()
        );
        eventPublisher.publish(ticketCancelledEvent);
        log.info("Event published:{}", ticketCancelledEvent);
    }


    public List<TicketDto> readByCurrentUser() {
        var currentUserId = userService.readCurrentUserId();
        return ticketRepository
                .readAllByUserId(currentUserId)
                .stream()
                .map(ticket -> {
                    var screeningDetails = screeningService.readScreeningDetails(
                            ticket.getScreeningId()
                    );
                    var seatDetails = screeningService.readSeatDetails(
                            ticket.getScreeningId(),
                            ticket.getSeatId()
                    );
                    return new TicketDto(
                            ticket.getId(),
                            ticket.getStatus(),
                            screeningDetails.filmTitle(),
                            screeningDetails.date(),
                            screeningDetails.roomId(),
                            seatDetails.rowNumber(),
                            seatDetails.seatNumber()
                    );
                })
                .toList();
    }

    private long timeToScreeningInHours(Clock clock, LocalDateTime screeningDate) {
        var currentDate = LocalDateTime.now(clock);
        return Duration
                .between(currentDate, screeningDate)
                .abs()
                .toHours();
    }
}
