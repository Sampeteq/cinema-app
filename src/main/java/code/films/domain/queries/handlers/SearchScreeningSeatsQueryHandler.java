package code.films.domain.queries.handlers;

import code.bookings.domain.Seat;
import code.films.domain.Screening;
import code.films.domain.ScreeningRepository;
import code.films.domain.queries.SearchScreeningSeatsQuery;
import code.films.infrastructure.exceptions.ScreeningNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SearchScreeningSeatsQueryHandler {

    private final ScreeningRepository screeningRepository;

    @Transactional(readOnly = true)
    public List<Seat> handle(SearchScreeningSeatsQuery query) {
        return screeningRepository
                .findById(query.screeningId())
                .map(Screening::getSeats)
                .orElseThrow(ScreeningNotFoundException::new);
    }
}
