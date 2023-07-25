package code.bookings.application.services;

import code.bookings.domain.Screening;
import code.bookings.domain.ports.ScreeningRepository;
import code.catalog.application.dto.SeatDto;
import code.catalog.application.dto.SeatMapper;
import code.shared.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SeatReadService {

    private final ScreeningRepository screeningRepository;
    private final SeatMapper seatMapper;

    @Transactional(readOnly = true)
    public List<SeatDto> readSeatsByScreeningId(Long screeningId) {
        return screeningRepository
                .readById(screeningId)
                .map(Screening::getSeats)
                .map(seatMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Screening"));
    }
}
