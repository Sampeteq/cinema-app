package code.catalog.application.services;

import code.catalog.application.dto.SeatDto;
import code.catalog.application.dto.SeatMapper;
import code.catalog.domain.Screening;
import code.catalog.infrastructure.db.ScreeningReadOnlyRepository;
import code.shared.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SeatReadService {

    private final ScreeningReadOnlyRepository screeningReadOnlyRepository;
    private final SeatMapper seatMapper;

    @Transactional(readOnly = true)
    public List<SeatDto> readByScreeningId(Long screeningId) {
        return screeningReadOnlyRepository
                .readById(screeningId)
                .map(Screening::getSeats)
                .map(seatMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Screening"));
    }
}
