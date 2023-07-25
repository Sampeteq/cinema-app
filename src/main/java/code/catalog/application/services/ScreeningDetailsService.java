package code.catalog.application.services;

import code.catalog.application.dto.ScreeningDetails;
import code.catalog.domain.ports.ScreeningReadOnlyRepository;
import code.shared.exceptions.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScreeningDetailsService {

    private final ScreeningReadOnlyRepository screeningReadOnlyRepository;

    public ScreeningDetails readBookingDataBySeatId(Long seatId) {
        return screeningReadOnlyRepository
                .readDetailsBySeatId(seatId)
                .orElseThrow(() -> new EntityNotFoundException("Seat"));
    }
}
