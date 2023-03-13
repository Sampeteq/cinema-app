package code.films.application.internal;

import code.bookings.application.events.BookingCancelledEvent;
import code.bookings.application.events.SeatBookedEvent;
import code.films.domain.Seat;
import code.films.domain.SeatRepository;
import code.films.domain.SeatStatus;
import code.films.infrastructure.exceptions.SeatNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.UUID;

@AllArgsConstructor
@Component
public class ScreeningEventHandler {

    private final SeatRepository seatRepository;

    @EventListener
    public void handle(SeatBookedEvent event) {
        searchSeatByIdOrThrow(event.seatId()).changeStatus(SeatStatus.BUSY);
    }

    @EventListener
    public void handle(BookingCancelledEvent event) {
        searchSeatByIdOrThrow(event.seatId()).changeStatus(SeatStatus.FREE);
    }

    private Seat searchSeatByIdOrThrow(UUID seatId) {
        return this.seatRepository
                .findById(seatId)
                .orElseThrow(SeatNotFoundException::new);
    }
}
