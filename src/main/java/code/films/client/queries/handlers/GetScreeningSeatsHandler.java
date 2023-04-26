package code.films.client.queries.handlers;

import code.films.client.dto.SeatDto;
import code.films.client.dto.mappers.SeatMapper;
import code.films.client.queries.GetScreeningSeatsQuery;
import code.films.domain.Screening;
import code.films.domain.ScreeningReadOnlyRepository;
import code.films.infrastructure.exceptions.ScreeningNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetScreeningSeatsHandler {

    private final ScreeningReadOnlyRepository screeningReadOnlyRepository;

    private final SeatMapper seatMapper;

    @Transactional(readOnly = true)
    public List<SeatDto> handle(GetScreeningSeatsQuery query) {
        return screeningReadOnlyRepository
                .getById(query.screeningId())
                .map(Screening::getSeats)
                .map(seatMapper::toDto)
                .orElseThrow(ScreeningNotFoundException::new);
    }
}
