package code.films.client.commands.events;

import code.bookings.client.commands.events.DecreasedFreeSeatsEvent;
import code.bookings.domain.exceptions.SeatNotAvailableException;
import code.films.domain.SeatReadOnlyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class DecreasedFreeSeatsHandler {

    private final SeatReadOnlyRepository seatReadOnlyRepository;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handle(DecreasedFreeSeatsEvent event) {
        seatReadOnlyRepository
                .getById(event.seatId())
                .orElseThrow(SeatNotAvailableException::new)
                .handleEvent(event);
    }
}