package code.films.application.internal;

import code.bookings.application.events.BookingCancelledEvent;
import code.bookings.application.events.SeatBookedEvent;
import code.films.domain.Seat;
import code.films.domain.SeatRepository;
import code.films.domain.SeatStatus;
import code.films.infrastructure.exceptions.SeatNotFoundException;
import com.google.common.eventbus.Subscribe;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@AllArgsConstructor
@Component
public class ScreeningEventHandler {

    private final SeatRepository seatRepository;

    @Subscribe
    public void handle(Object event) {
        if (event instanceof SeatBookedEvent seatBookedEvent) {
            searchSeatByIdOrThrow(seatBookedEvent.seatId()).changeStatus(SeatStatus.BUSY);
        } else if (event instanceof BookingCancelledEvent bookingCancelledEvent) {
            searchSeatByIdOrThrow(bookingCancelledEvent.seatId()).changeStatus(SeatStatus.FREE);
        } else {
            throw new IllegalArgumentException("Not supported event type");
        }
    }

    private Seat searchSeatByIdOrThrow(UUID seatId) {
        return this.seatRepository
                .findById(seatId)
                .orElseThrow(SeatNotFoundException::new);
    }
}
