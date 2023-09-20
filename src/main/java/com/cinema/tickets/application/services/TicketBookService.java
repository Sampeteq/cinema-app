package com.cinema.tickets.application.services;

import com.cinema.catalog.domain.ScreeningReadOnlyRepository;
import com.cinema.shared.exceptions.EntityNotFoundException;
import com.cinema.shared.time.TimeProvider;
import com.cinema.tickets.application.dto.TicketBookDto;
import com.cinema.tickets.domain.Ticket;
import com.cinema.tickets.domain.TicketRepository;
import com.cinema.user.application.services.UserFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
class TicketBookService {

    private final ScreeningReadOnlyRepository screeningReadOnlyRepository;
    private final UserFacade userFacade;
    private final TimeProvider timeProvider;
    private final TicketRepository ticketRepository;

    @Transactional
    public void bookTicket(TicketBookDto dto) {
        var screening = screeningReadOnlyRepository
                .readByIdWithSeats(dto.screeningId())
                .orElseThrow(() -> new EntityNotFoundException("Screening"));
        var currentUserId = userFacade.readCurrentUserId();
        var ticket = Ticket.book(
                timeProvider.getCurrentDate(),
                screening,
                dto.rowNumber(),
                dto.seatNumber(),
                currentUserId
        );
        var addedTicket = ticketRepository.add(ticket);
        log.info("Added a ticket:{}", addedTicket);
    }
}
