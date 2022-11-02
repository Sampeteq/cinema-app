package code.reservations;

import code.reservations.dto.ReserveScreeningTicketDTO;
import code.reservations.dto.ScreeningTicketReservationCancelledEvent;
import code.reservations.dto.ScreeningTicketReservedEvent;
import code.screenings.ScreeningFacade;
import com.google.common.eventbus.EventBus;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;

@AllArgsConstructor
class ScreeningTicketReservationService {

    private final ScreeningFacade screeningFacade;

    private final EventBus eventBus;

    public ScreeningTicket reserve(ReserveScreeningTicketDTO dto, Clock clock) {
        var screeningReservationData = screeningFacade.fetchReservationData(dto.screeningId());
        var ticket = ScreeningTicket.reserve(dto, screeningReservationData, clock);
        eventBus.post(
                new ScreeningTicketReservedEvent(dto.screeningId())
        );
        return ticket;
    }

    public void cancelTicket(ScreeningTicket ticket, Clock clock) {
        var screeningReservationData = screeningFacade.fetchReservationData(ticket.getScreeningId());
        ticket.cancel(screeningReservationData.screeningDate(), clock);
        eventBus.post(
                new ScreeningTicketReservationCancelledEvent(ticket.getScreeningId())
        );
    }
}
