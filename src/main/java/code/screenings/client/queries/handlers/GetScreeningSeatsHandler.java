package code.screenings.client.queries.handlers;

import code.screenings.client.dto.SeatDto;
import code.screenings.client.dto.SeatMapper;
import code.screenings.client.exceptions.ScreeningNotFoundException;
import code.screenings.client.queries.GetScreeningSeatsQuery;
import code.screenings.domain.Screening;
import code.screenings.domain.ScreeningReadOnlyRepository;
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
