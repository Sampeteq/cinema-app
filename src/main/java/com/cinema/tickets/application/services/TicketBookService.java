package com.cinema.tickets.application.services;

import com.cinema.catalog.application.services.CatalogFacade;
import com.cinema.shared.events.EventPublisher;
import com.cinema.shared.time.TimeProvider;
import com.cinema.tickets.application.dto.TicketBookDto;
import com.cinema.tickets.domain.Ticket;
import com.cinema.tickets.domain.TicketRepository;
import com.cinema.tickets.domain.events.TicketBookedEvent;
import com.cinema.tickets.domain.exceptions.TicketAlreadyExists;
import com.cinema.users.application.services.UserFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
class TicketBookService {

    private final TicketRepository ticketRepository;
    private final CatalogFacade catalogFacade;
    private final UserFacade userFacade;
    private final TimeProvider timeProvider;
    private final EventPublisher eventPublisher;

    @Transactional
    public void bookTicket(TicketBookDto dto) {
        if (ticketRepository.exists(dto.screeningId(), dto.rowNumber(), dto.seatNumber())) {
            throw new TicketAlreadyExists();
        }
        var screeningDetails = catalogFacade.readScreeningDetails(
                dto.screeningId(),
                dto.rowNumber(),
                dto.rowNumber()
        );
        var currentUserId = userFacade.readCurrentUserId();
        var ticket = new Ticket(
                screeningDetails.filmTitle(),
                dto.screeningId(),
                screeningDetails.date(),
                screeningDetails.roomCustomId(),
                dto.rowNumber(),
                dto.seatNumber()
        );
        ticket.book(timeProvider.getCurrentDate(), currentUserId);
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
