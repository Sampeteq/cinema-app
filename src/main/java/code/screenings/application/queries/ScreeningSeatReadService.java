package code.screenings.application.queries;

import code.screenings.application.dto.SeatDto;
import code.screenings.application.dto.SeatMapper;
import code.screenings.domain.Screening;
import code.screenings.domain.ScreeningReadOnlyRepository;
import code.shared.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ScreeningSeatReadService {

    private final ScreeningReadOnlyRepository screeningReadOnlyRepository;
    private final SeatMapper seatMapper;

    @Transactional(readOnly = true)
    public List<SeatDto> handle(ScreeningSeatQuery query) {
        return screeningReadOnlyRepository
                .getById(query.screeningId())
                .map(Screening::getSeats)
                .map(seatMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Screening"));
    }
}
