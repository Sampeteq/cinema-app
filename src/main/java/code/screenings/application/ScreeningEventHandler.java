package code.screenings.application;

import code.bookings.application.dto.BookingCancelledEvent;
import code.bookings.application.dto.SeatBookedEvent;
import code.screenings.domain.ScreeningRepository;
import code.screenings.domain.Seat;
import code.screenings.domain.SeatStatus;
import code.screenings.infrastructure.exceptions.ScreeningNotFoundException;
import code.screenings.infrastructure.exceptions.SeatNotFoundException;
import com.google.common.eventbus.Subscribe;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@AllArgsConstructor
@Component
public class ScreeningEventHandler {

    private final ScreeningRepository screeningRepository;

    @Subscribe
    public void handle(Object event) {
        if (event instanceof SeatBookedEvent seatBookedEvent) {
            searchSeatByIdOrThrow(
                    seatBookedEvent.screeningId(),
                    seatBookedEvent.seatId()
            ).changeStatus(SeatStatus.BUSY);
        } else if (event instanceof BookingCancelledEvent bookingCancelledEvent) {
            searchSeatByIdOrThrow(
                    bookingCancelledEvent.screeningId(),
                    bookingCancelledEvent.seatId()
            ).changeStatus(SeatStatus.FREE);
        } else {
            throw new IllegalArgumentException("Not supported event type");
        }
    }

    private Seat searchSeatByIdOrThrow(UUID screeningId, UUID seatId) {
        return screeningRepository
                .findById(screeningId)
                .orElseThrow(ScreeningNotFoundException::new)
                .getSeat(seatId)
                .orElseThrow(SeatNotFoundException::new);
    }
}
