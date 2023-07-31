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

    public ScreeningDetails readScreeningDetails(Long screeningId) {
        return screeningReadOnlyRepository
                .readDetailsBySeatId(screeningId)
                .orElseThrow(() -> new EntityNotFoundException("Screening"));
    }
}
