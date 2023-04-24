package code.films.client.queries.handlers;

import code.bookings.domain.exceptions.SeatNotAvailableException;
import code.films.client.dto.SeatDetails;
import code.films.domain.SeatReadOnlyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class GetSeatDetailsHandler {

    private final SeatReadOnlyRepository seatReadOnlyRepository;

    private final Clock clock;

    public SeatDetails handle(UUID seatId) {
        var seat = seatReadOnlyRepository
                .getById(seatId)
                .orElseThrow(SeatNotAvailableException::new);
        return SeatDetails
                .builder()
                .isAvailable(seat.isFree())
                .timeToScreeningInHour(seat.getScreening().timeToScreeningStartInHours(clock))
                .build();
    }
}