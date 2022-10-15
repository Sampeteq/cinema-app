package code.ticket;

import code.screening.ScreeningFacade;
import code.screening.exception.NoScreeningFreeSeatsException;
import code.ticket.dto.BookTicketDTO;
import code.ticket.dto.TicketDTO;
import code.ticket.exception.TicketAlreadyCancelledException;
import code.ticket.exception.TicketNotFoundException;
import code.ticket.exception.TooLateToBookTicketException;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
public class TicketFacade {

    private final TicketRepository ticketRepository;
    private final ScreeningFacade screeningFacade;

    @Transactional
    public TicketDTO book(BookTicketDTO dto, Clock clock) {
        var screeningData = screeningFacade.readTicketData(dto.screeningId());
        if (Duration
                .between(LocalDateTime.now(clock), screeningData.screeningDate())
                .abs()
                .toHours() < 24) {
            throw new TooLateToBookTicketException();
        }
        if (screeningData.screeningFreeSeats() < 0) {
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

    public TicketDTO read(UUID ticketId) {
        return getTicketOrThrowException(ticketId).toDTO();
    }

    public List<TicketDTO> readAll() {
        return ticketRepository
                .findAll()
                .stream()
                .map(Ticket::toDTO)
                .toList();
    }

    private Ticket getTicketOrThrowException(UUID ticketId) {
        return ticketRepository
                .findByUuid(ticketId)
                .orElseThrow(() -> new TicketNotFoundException(ticketId));
    }
}
