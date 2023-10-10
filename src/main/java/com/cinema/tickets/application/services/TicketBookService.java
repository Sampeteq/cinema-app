package com.cinema.tickets.application.services;

import com.cinema.repertoire.application.services.RepertoireFacade;
import com.cinema.shared.events.EventPublisher;
import com.cinema.shared.exceptions.EntityNotFoundException;
import com.cinema.tickets.application.dto.TicketBookDto;
import com.cinema.tickets.domain.Ticket;
import com.cinema.tickets.domain.TicketRepository;
import com.cinema.tickets.domain.events.TicketBookedEvent;
import com.cinema.tickets.domain.exceptions.TicketAlreadyExists;
import com.cinema.users.application.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;

@Service
@RequiredArgsConstructor
@Slf4j
class TicketBookService {

    private final TicketRepository ticketRepository;
    private final RepertoireFacade repertoireFacade;
    private final UserService userService;
    private final Clock clock;
    private final EventPublisher eventPublisher;

    @Transactional
    public void bookTicket(TicketBookDto dto) {
        if (ticketRepository.exists(dto.screeningId(), dto.rowNumber(), dto.seatNumber())) {
            throw new TicketAlreadyExists();
        }
        var screeningDetails = repertoireFacade.readScreeningDetails(
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
}
