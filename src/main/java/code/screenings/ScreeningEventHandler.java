package code.screenings;

import code.bookings.dto.ScreeningTicketBookedEvent;
import code.bookings.dto.ScreeningTicketBookingCancelledEvent;
import code.screenings.exception.ScreeningNotFoundException;
import com.google.common.eventbus.Subscribe;
import lombok.AllArgsConstructor;

@AllArgsConstructor
class ScreeningEventHandler {

    private final ScreeningRepository screeningRepository;

    @Subscribe
    void handle(ScreeningTicketBookedEvent event) {
        var screening = screeningRepository
                .findById(event.screeningId())
                .orElseThrow(() -> new ScreeningNotFoundException(event.screeningId()));
        screening.decreaseFreeSeatsByOne();
        screeningRepository.save(screening);
    }

    @Subscribe
    void handle(ScreeningTicketBookingCancelledEvent event) {
        var screening = screeningRepository
                .findById(event.screeningId())
                .orElseThrow(() -> new ScreeningNotFoundException(event.screeningId()));
        screening.increaseFreeSeatsByOne();
        screeningRepository.save(screening);
    }
}
