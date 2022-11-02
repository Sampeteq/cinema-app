package code.reservations;

import code.reservations.dto.ReserveScreeningTicketDTO;
import code.reservations.dto.ScreeningTicketReservationCancelledEvent;
import code.reservations.dto.ScreeningTicketReservedEvent;
import code.screenings.ScreeningFacade;
import com.google.common.eventbus.EventBus;
import lombok.AllArgsConstructor;

import java.time.Clock;

@AllArgsConstructor
class ScreeningTicketReservationService {

    private final ScreeningFacade screeningFacade;

    private final EventBus eventBus;

    public ScreeningTicket reserve(ReserveScreeningTicketDTO dto, Clock clock) {
        var reservationData = screeningFacade.fetchReservationData(dto.screeningId());
        var ticket = new ScreeningTicket(dto.firstName(), dto.lastName(), dto.screeningId());
        ticket.reserve(reservationData.screeningDate(), reservationData.screeningFreeSeats(), clock);
        eventBus.post(
                new ScreeningTicketReservedEvent(dto.screeningId())
        );
        return ticket;
    }

    public void cancelTicket(ScreeningTicket ticket, Clock clock) {
        var screeningDate = screeningFacade.fetchScreeningDate(ticket.getScreeningId());
        ticket.cancel(screeningDate, clock);
        eventBus.post(
                new ScreeningTicketReservationCancelledEvent(ticket.getScreeningId())
        );
    }
}
