package code.films.domain.client.queries.handlers;

import code.films.domain.Seat;
import code.films.domain.Screening;
import code.films.domain.ScreeningReadOnlyRepository;
import code.films.domain.client.queries.GetScreeningSeatsQuery;
import code.films.infrastructure.exceptions.ScreeningNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetScreeningSeatsHandler {

    private final ScreeningReadOnlyRepository screeningReadOnlyRepository;

    @Transactional(readOnly = true)
    public List<Seat> handle(GetScreeningSeatsQuery query) {
        return screeningReadOnlyRepository
                .getById(query.screeningId())
                .map(Screening::getSeats)
                .orElseThrow(ScreeningNotFoundException::new);
    }
}
