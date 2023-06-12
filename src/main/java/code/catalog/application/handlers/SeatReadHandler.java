package code.catalog.application.handlers;

import code.catalog.application.dto.SeatDto;
import code.catalog.domain.Screening;
import code.catalog.application.dto.SeatMapper;
import code.catalog.infrastructure.db.ScreeningReadOnlyRepository;
import code.shared.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SeatReadHandler {

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
