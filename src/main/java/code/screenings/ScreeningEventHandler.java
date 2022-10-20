package code.screenings;

import code.reservations.dto.ScreeningTicketReservationCancelledEvent;
import code.reservations.dto.ScreeningTicketReservedEvent;
import code.screenings.exception.ScreeningNotFoundException;
import com.google.common.eventbus.Subscribe;
import lombok.AllArgsConstructor;

@AllArgsConstructor
class ScreeningEventHandler {

    private final ScreeningRepository screeningRepository;

    @Subscribe
    void handle(ScreeningTicketReservedEvent event) {
        var screening = screeningRepository
                .findById(event.screeningId())
                .orElseThrow(() -> new ScreeningNotFoundException(event.screeningId()));
        screening.decreaseFreeSeatsByOne();
        screeningRepository.save(screening);
    }

    @Subscribe
    void handle(ScreeningTicketReservationCancelledEvent event) {
        var screening = screeningRepository
                .findById(event.screeningId())
                .orElseThrow(() -> new ScreeningNotFoundException(event.screeningId()));
        screening.increaseFreeSeatsByOne();
        screeningRepository.save(screening);
    }
}
