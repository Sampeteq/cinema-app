package code.screenings.application.handlers;

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
public class ScreeningSeatReadHandler {

    private final ScreeningReadOnlyRepository screeningReadOnlyRepository;
    private final SeatMapper seatMapper;

    @Transactional(readOnly = true)
    public List<SeatDto> handle(Long screeningId) {
        return screeningReadOnlyRepository
                .findById(screeningId)
                .map(Screening::getSeats)
                .map(seatMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Screening"));
    }
}
