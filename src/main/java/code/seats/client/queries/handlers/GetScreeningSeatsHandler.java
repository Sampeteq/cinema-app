package code.seats.client.queries.handlers;

import code.screenings.domain.Screening;
import code.screenings.domain.ScreeningReadOnlyRepository;
import code.screenings.client.exceptions.ScreeningNotFoundException;
import code.seats.client.dto.SeatDto;
import code.seats.client.dto.SeatMapper;
import code.seats.client.queries.GetScreeningSeatsQuery;
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
