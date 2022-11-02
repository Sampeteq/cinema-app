package code.reservations;

import code.reservations.dto.ReserveScreeningTicketDTO;
import code.reservations.dto.TicketDTO;
import code.reservations.exception.ScreeningTicketNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
public class ReservationFacade {

    private final ScreeningTicketRepository screeningTicketRepository;

    private final ScreeningTicketReservationService screeningTicketReservationService;

    @Transactional
    public TicketDTO reserveTicket(ReserveScreeningTicketDTO dto, Clock clock) {
        var ticket = screeningTicketReservationService.reserve(dto, clock);
        return screeningTicketRepository
                .save(ticket)
                .toDTO();
    }

    @Transactional
    public void cancelTicket(UUID ticketId, Clock clock) {
        var ticket = getTicketOrThrow(ticketId);
        screeningTicketReservationService.cancelTicket(ticket, clock);
    }

    public TicketDTO readTicket(UUID ticketId) {
        return getTicketOrThrow(ticketId).toDTO();
    }

    public List<TicketDTO> readAllTickets() {
        return screeningTicketRepository
                .findAll()
                .stream()
                .map(ScreeningTicket::toDTO)
                .toList();
    }

    private ScreeningTicket getTicketOrThrow(UUID ticketId) {
        return screeningTicketRepository
                .findByUuid(ticketId)
                .orElseThrow(() -> new ScreeningTicketNotFoundException(ticketId));
    }
}
