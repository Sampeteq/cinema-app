package com.cinema.tickets.application.services;

import com.cinema.repertoire.application.services.ScreeningService;
import com.cinema.shared.events.EventPublisher;
import com.cinema.shared.exceptions.EntityNotFoundException;
import com.cinema.tickets.application.dto.TicketBookDto;
import com.cinema.tickets.application.dto.TicketDto;
import com.cinema.tickets.application.dto.TicketMapper;
import com.cinema.tickets.domain.Ticket;
import com.cinema.tickets.domain.TicketRepository;
import com.cinema.tickets.domain.events.TicketBookedEvent;
import com.cinema.tickets.domain.events.TicketCancelledEvent;
import com.cinema.tickets.domain.exceptions.TicketAlreadyExists;
import com.cinema.tickets.domain.exceptions.TicketNotBelongsToUser;
import com.cinema.users.application.services.UserService;
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
    private final TicketMapper ticketMapper;
    private final ScreeningService screeningService;
    private final UserService userService;
    private final Clock clock;
    private final EventPublisher eventPublisher;

    @Transactional
    public void bookTicket(TicketBookDto dto) {
        if (ticketRepository.exists(dto.screeningId(), dto.rowNumber(), dto.seatNumber())) {
            throw new TicketAlreadyExists();
        }
        var screeningDetails = screeningService.readScreeningDetails(
                dto.screeningId(),
                dto.rowNumber(),
                dto.rowNumber()
        );
        if (!screeningDetails.seatExists()) {
            throw new EntityNotFoundException("Seat");
        }
        var ticket = new Ticket(
                screeningDetails.filmTitle(),
                dto.screeningId(),
                screeningDetails.date(),
                screeningDetails.roomId(),
                dto.rowNumber(),
                dto.seatNumber()
        );
        var currentUserId = userService.readCurrentUserId();
        ticket.book(clock, currentUserId);
        var addedTicket = ticketRepository.add(ticket);
        log.info("Added a ticket:{}", addedTicket);
        var ticketBookedEvent = new TicketBookedEvent(
                dto.screeningId(),
                dto.rowNumber(),
                dto.seatNumber()
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
        ticket.cancel(clock);
        var ticketCancelledEvent = new TicketCancelledEvent(
                ticket.getScreeningId(),
                ticket.getRowNumber(),
                ticket.getSeatNumber()
        );
        eventPublisher.publish(ticketCancelledEvent);
        log.info("Event published:{}", ticketCancelledEvent);
    }


    public List<TicketDto> readByCurrentUser() {
        var currentUserId = userService.readCurrentUserId();
        return ticketRepository
                .readAllByUserId(currentUserId)
                .stream()
                .map(ticketMapper::mapToDto)
                .toList();
    }
}
