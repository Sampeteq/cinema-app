package code.catalog.application.services;

import code.catalog.application.dto.BookingDataDto;
import code.catalog.infrastructure.db.ScreeningReadOnlyRepository;
import code.shared.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingDataService {

    private final ScreeningReadOnlyRepository screeningReadOnlyRepository;

    public BookingDataDto readBookingDataBySeatId(Long seatId) {
        return screeningReadOnlyRepository
                .readBookingDataBySeatId(seatId, PageRequest.of(0, 1))
                .stream()
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Seat"));
    }
}
