package code.bookings;

import code.bookings.dto.BookScreeningTicketDTO;
import code.bookings.dto.ScreeningTicketBookedEvent;
import code.bookings.dto.ScreeningTicketBookingCancelledEvent;
import code.screenings.ScreeningFacade;
import com.google.common.eventbus.EventBus;
import lombok.AllArgsConstructor;

import java.time.Clock;

@AllArgsConstructor
class ScreeningTicketBookingService {

    private final ScreeningFacade screeningFacade;

    private final EventBus eventBus;

    public ScreeningTicket book(BookScreeningTicketDTO dto, Clock clock) {
        var reservationData = screeningFacade.fetchBookingData(dto.screeningId());
        var ticket = new ScreeningTicket(dto.firstName(), dto.lastName(), dto.screeningId());
        ticket.book(reservationData.screeningDate(), reservationData.screeningFreeSeats(), clock);
        eventBus.post(
                new ScreeningTicketBookedEvent(dto.screeningId())
        );
        return ticket;
    }

    public void cancel(ScreeningTicket ticket, Clock clock) {
        var screeningDate = screeningFacade.fetchScreeningDate(ticket.getScreeningId());
        ticket.cancel(screeningDate, clock);
        eventBus.post(
                new ScreeningTicketBookingCancelledEvent(ticket.getScreeningId())
        );
    }
}
