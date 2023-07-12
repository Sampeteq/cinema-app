package code.catalog.application.services;

import code.catalog.application.dto.SeatDataDto;
import code.catalog.infrastructure.db.SeatRepository;
import code.shared.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SeatDataService {

    private final SeatRepository seatRepository;

    public SeatDataDto readBookingDataBySeatId(Long seatId) {
        return seatRepository
                .readBookingDataBySeatId(seatId, PageRequest.of(0, 1))
                .stream()
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Seat"));
    }
}
