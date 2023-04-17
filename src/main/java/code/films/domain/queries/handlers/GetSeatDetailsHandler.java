package code.films.domain.queries.handlers;

import code.films.domain.SeatReadOnlyRepository;
import code.bookings.domain.exceptions.SeatNotAvailableException;
import code.films.domain.queries.GetSeatDetailsQuery;
import code.films.domain.queries.SeatDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Clock;

@Component
@RequiredArgsConstructor
public class GetSeatDetailsHandler {

    private final SeatReadOnlyRepository seatReadOnlyRepository;

    private final Clock clock;

    public SeatDetails handle(GetSeatDetailsQuery query) {
        var seat = seatReadOnlyRepository
                .getById(query.seatid())
                .orElseThrow(SeatNotAvailableException::new);
        return SeatDetails
                .builder()
                .isAvailable(seat.isFree())
                .timeToScreeningInHour(seat.getScreening().timeToScreeningStartInHours(clock))
                .build();
    }
}
