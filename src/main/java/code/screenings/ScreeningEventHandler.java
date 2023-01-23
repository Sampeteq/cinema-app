package code.screenings;

import code.bookings.dto.BookingCancelledEvent;
import code.bookings.dto.SeatBookedEvent;
import code.screenings.exception.ScreeningNotFoundException;
import code.screenings.exception.SeatNotFoundException;
import com.google.common.eventbus.Subscribe;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@AllArgsConstructor
class ScreeningEventHandler {

    private final ScreeningRepository screeningRepository;

    @Subscribe
    void handle(SeatBookedEvent event) {
        searchSeatByIdOrThrow(event.screeningId(), event.seatId()).changeStatus(SeatStatus.BUSY);
    }

    @Subscribe
    void handle(BookingCancelledEvent event) {
        searchSeatByIdOrThrow(event.screeningId(), event.seatId()).changeStatus(SeatStatus.FREE);
    }

    private Seat searchSeatByIdOrThrow(UUID screeningId, UUID seatId) {
        return screeningRepository
                .findById(screeningId)
                .orElseThrow(ScreeningNotFoundException::new)
                .getSeat(seatId)
                .orElseThrow(SeatNotFoundException::new);
    }
}
