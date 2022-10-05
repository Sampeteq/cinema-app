package com.example.ticket.domain;

import com.example.screening.domain.ScreeningFacade;
import com.example.ticket.domain.dto.ReserveTicketDTO;
import com.example.ticket.domain.dto.TicketDTO;
import com.example.ticket.domain.exception.TicketAlreadyCancelledException;
import com.example.ticket.domain.exception.TicketNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.util.List;

@AllArgsConstructor
public class TicketFacade {

    private final TicketRepository ticketRepository;
    private final TicketFactory ticketFactory;
    private final ScreeningFacade screeningFacade;

    @Transactional
    public TicketDTO reserve(ReserveTicketDTO dto) {
        screeningFacade.checkReservationPossibility(dto.screeningId(), dto.age());
        var ticket = ticketFactory.createTicket(dto);
        var addedTicket = ticketRepository.save(ticket);
        screeningFacade.decreaseFreeSeatsByOne(dto.screeningId());
        return addedTicket.toDTO();
    }

    @Transactional
    public void cancel(Long ticketId, Clock clock) {
        var ticket = getTicketOrThrowException(ticketId);
        if (ticket.isAlreadyCancelled()) {
            throw new TicketAlreadyCancelledException(ticketId);
        }
        screeningFacade.checkCancelReservationPossibility(ticket.getScreeningId(), clock);
        ticket.cancel();
    }

    public TicketDTO read(Long ticketId) {
        return getTicketOrThrowException(ticketId).toDTO();
    }

    public List<TicketDTO> readAll() {
        return ticketRepository
                .findAll()
                .stream()
                .map(Ticket::toDTO)
                .toList();
    }

    private Ticket getTicketOrThrowException(Long ticketId) {
        return ticketRepository
                .findById(ticketId)
                .orElseThrow(() -> new TicketNotFoundException(ticketId));
    }
}
