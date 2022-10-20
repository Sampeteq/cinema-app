package code.screening;

import code.reservation.dto.ScreeningTicketReservedEvent;
import code.screening.exception.ScreeningException;
import com.google.common.eventbus.Subscribe;
import lombok.AllArgsConstructor;

@AllArgsConstructor
class ScreeningEventHandler {

    private final ScreeningRepository screeningRepository;

    @Subscribe
    void handle(ScreeningTicketReservedEvent event) {
        var screening = screeningRepository
                .findById(event.screeningId())
                .orElseThrow(() -> ScreeningException.screeningNotFound(event.screeningId()));
        screening.decreaseFreeSeatsByOne();
        screeningRepository.save(screening);
    }
}
