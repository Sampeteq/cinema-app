package code.films.domain.events;

import code.bookings.domain.events.IncreasedFreeSeatsEvent;
import code.bookings.domain.exceptions.SeatNotAvailableException;
import code.films.domain.SeatReadOnlyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class IncreasedFreeSeatsHandler {

    private final SeatReadOnlyRepository seatReadOnlyRepository;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handle(IncreasedFreeSeatsEvent event) {
        seatReadOnlyRepository
                .getById(event.seatId())
                .orElseThrow(SeatNotAvailableException::new)
                .handleEvent(event);
    }
}
