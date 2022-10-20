package code.screening;

import code.reservation.dto.ScreeningTicketReservationCancelledEvent;
import code.reservation.dto.ScreeningTicketReservedEvent;
import code.screening.exception.ScreeningNotFoundException;
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
