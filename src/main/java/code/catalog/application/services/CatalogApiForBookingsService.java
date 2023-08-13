package code.catalog.application.services;

import code.catalog.application.dto.ScreeningDetailsDto;
import code.catalog.domain.ports.ScreeningReadOnlyRepository;
import code.shared.exceptions.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CatalogApiForBookingsService {

    private final ScreeningReadOnlyRepository screeningReadOnlyRepository;

    public ScreeningDetailsDto readScreeningDetails(Long screeningId) {
        return screeningReadOnlyRepository
                .readDetailsBySeatId(screeningId)
                .orElseThrow(() -> new EntityNotFoundException("Screening"));
    }
}
