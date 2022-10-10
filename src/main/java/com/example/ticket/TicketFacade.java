package com.example.ticket;

import com.example.screening.ScreeningFacade;
import com.example.screening.exception.NoScreeningFreeSeatsException;
import com.example.ticket.dto.ReserveTicketDTO;
import com.example.ticket.dto.TicketDTO;
import com.example.ticket.exception.TicketAlreadyCancelledException;
import com.example.ticket.exception.TicketNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
public class TicketFacade {

    private final TicketRepository ticketRepository;
    private final ScreeningFacade screeningFacade;

    @Transactional
    public TicketDTO reserve(ReserveTicketDTO dto) {
        var screeningData= screeningFacade.readTicketData(dto.screeningId());
        if (screeningData.screeningFreeSeats() < 0){
            throw new NoScreeningFreeSeatsException(dto.screeningId());
        }
        var ticket = new Ticket(dto.firstName(), dto.lastName(), dto.screeningId());
        var addedTicket = ticketRepository.save(ticket);
        screeningFacade.decreaseFreeSeatsByOne(dto.screeningId());
        return addedTicket.toDTO();
    }

    @Transactional
    public void cancel(UUID ticketId, Clock clock) {
        var ticket = ticketRepository
                .findByUuid(ticketId)
                .orElseThrow(() -> new TicketNotFoundException(ticketId));
        if (ticket.isAlreadyCancelled()) {
            throw new TicketAlreadyCancelledException(ticketId);
        }
        var screeningData = screeningFacade.readTicketData(ticket.getScreeningId());
        ticket.cancel(screeningData.screeningDate(), clock);
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
