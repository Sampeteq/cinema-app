package code.films.applications.services;

import code.films.applications.dto.ScreeningDto;
import code.films.applications.dto.ScreeningSearchParams;
import code.bookings.application.dto.SeatDto;
import code.films.applications.services.mappers.ScreeningMapper;
import code.films.domain.ScreeningRepository;
import code.bookings.domain.Seat;
import code.bookings.domain.SeatRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@AllArgsConstructor
public class ScreeningSearchService {

    private final ScreeningRepository screeningRepository;

    private final SeatRepository seatRepository;

    private final ScreeningMapper screeningMapper;

    public List<ScreeningDto> searchScreeningsBy(ScreeningSearchParams params) {
        return screeningRepository
                .findBy(params)
                .stream()
                .map(screeningMapper::mapToDto)
                .toList();
    }

    public List<SeatDto> searchSeats(UUID screeningId) {
        return seatRepository
                .findByScreening_Id(screeningId)
                .stream()
                .map(Seat::toDto)
                .toList();
    }
}
