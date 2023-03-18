package code.films.application.services;

import code.films.application.dto.ScreeningDto;
import code.films.application.dto.ScreeningSearchParams;
import code.films.application.dto.SeatDetails;
import code.films.application.dto.SeatDto;
import code.films.application.services.mappers.ScreeningMapper;
import code.films.domain.ScreeningRepository;
import code.films.domain.Seat;
import code.films.domain.SeatRepository;
import code.films.infrastructure.exceptions.SeatNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.util.List;
import java.util.UUID;

@Component
@AllArgsConstructor
public class ScreeningSearchService {

    private final ScreeningRepository screeningRepository;

    private final SeatRepository seatRepository;

    private final ScreeningMapper screeningMapper;

    private final Clock clock;

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

    public SeatDetails searchScreeningDetails(UUID seatId) {
        var seat = seatRepository
                .findById(seatId)
                .orElseThrow(SeatNotFoundException::new);
        var screening = seat.getScreening();
        var timeToScreeningStartInHours = screening.timeToScreeningStartInHours(clock);
        var isFree = seat.isFree();
        return new SeatDetails(
                isFree,
                timeToScreeningStartInHours,
                screening.getId()
        );
    }
}
