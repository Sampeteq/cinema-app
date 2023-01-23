package code.screenings;

import code.bookings.dto.BookingCancelledEvent;
import code.bookings.dto.SeatBookedEvent;
import code.screenings.exception.ScreeningNotFoundException;
import code.screenings.exception.SeatNotFoundException;
import com.google.common.eventbus.Subscribe;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@AllArgsConstructor
class ScreeningEventHandler {

    private final ScreeningRepository screeningRepository;

    @Subscribe
    void handle(Object event) {
        if (event instanceof SeatBookedEvent seatBookedEvent) {
            searchSeatByIdOrThrow(
                    seatBookedEvent.screeningId(),
                    seatBookedEvent.seatId()
            ).changeStatus(SeatStatus.BUSY);
        }
        else if (event instanceof BookingCancelledEvent bookingCancelledEvent) {
            searchSeatByIdOrThrow(
                    bookingCancelledEvent.screeningId(),
                    bookingCancelledEvent.seatId()
            ).changeStatus(SeatStatus.FREE);
        }
        else {
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
