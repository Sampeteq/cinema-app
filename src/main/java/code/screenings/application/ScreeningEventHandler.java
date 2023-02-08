package code.screenings.application;

import code.bookings.application.dto.BookingCancelledEvent;
import code.bookings.application.dto.SeatBookedEvent;
import code.screenings.domain.Seat;
import code.screenings.domain.SeatRepository;
import code.screenings.domain.SeatStatus;
import code.screenings.infrastructure.exceptions.SeatNotFoundException;
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
