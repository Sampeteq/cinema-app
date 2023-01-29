package code.screenings.application;

import code.screenings.domain.Screening;
import code.screenings.domain.ScreeningRepository;
import code.screenings.application.dto.ScreeningDetails;
import code.screenings.application.dto.ScreeningDto;
import code.screenings.application.dto.SeatDto;
import code.screenings.infrastructure.exceptions.ScreeningNotFoundException;
import code.screenings.infrastructure.exceptions.SeatNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.util.List;
import java.util.UUID;



@Component
@AllArgsConstructor
public class ScreeningSearcher {

    private final ScreeningRepository screeningRepository;

    public List<ScreeningDto> searchScreeningsBy(ScreeningSearchParams params) {
        return screeningRepository
                .findBy(params)
                .stream()
                .map(Screening::toDto)
                .toList();
    }

    List<SeatDto> searchSeats(UUID screeningId) {
        return screeningRepository
                .findById(screeningId)
                .map(Screening::seatsDtos)
                .orElseThrow(ScreeningNotFoundException::new);
    }

    ScreeningDetails searchScreeningDetails(UUID screeningId, UUID seatId, Clock clock) {
        var screening = screeningRepository
                .findById(screeningId)
                .orElseThrow(ScreeningNotFoundException::new);
        var timeToScreeningStartInHours = screening.timeToScreeningStartInHours(clock);
        var isFree = screening
                .getSeat(seatId)
                .orElseThrow(SeatNotFoundException::new)
                .isFree();
        return new ScreeningDetails(
                timeToScreeningStartInHours,
                isFree
        );
    }
}
