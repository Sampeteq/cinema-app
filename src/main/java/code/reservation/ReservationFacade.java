package code.reservation;

import code.reservation.dto.ReserveScreeningTicketDTO;
import code.reservation.dto.ScreeningTicketReservedEvent;
import code.reservation.dto.TicketDTO;
import code.reservation.exception.ScreeningTicketNotFoundException;
import code.screening.ScreeningFacade;
import com.google.common.eventbus.EventBus;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
public class ReservationFacade {

    private final ScreeningFacade screeningFacade;

    private final ScreeningTicketRepository screeningTicketRepository;

    private final EventBus eventBus;

    @Transactional
    public TicketDTO reserveTicket(ReserveScreeningTicketDTO reserveTicketDTO, Clock clock) {
        var screeningReservationData = screeningFacade.fetchReservationData(reserveTicketDTO.screeningId());
        var ticket = ScreeningTicket.reserve(reserveTicketDTO, screeningReservationData, clock);
        eventBus.post(
                new ScreeningTicketReservedEvent(reserveTicketDTO.screeningId())
        );
        return screeningTicketRepository
                .save(ticket)
                .toDTO();
    }

    @Transactional
    public void cancelTicket(UUID ticketId, Clock clock) {
        var ticket = getTicketOrThrow(ticketId);
        var screeningReservationData = screeningFacade.fetchReservationData(ticket.getScreeningId());
        ticket.cancel(screeningReservationData.screeningDate(), clock);
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
