package code.films.domain.events;

import code.bookings.domain.events.DecreasedFreeSeatsEvent;
import code.bookings.domain.exceptions.SeatNotAvailableException;
import code.films.domain.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class DecreasedFreeSeatsHandler {

    private final SeatRepository seatRepository;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handle(DecreasedFreeSeatsEvent event) {
        seatRepository
                .findById(event.seatId())
                .orElseThrow(SeatNotAvailableException::new)
                .handleEvent(event);
    }
}
