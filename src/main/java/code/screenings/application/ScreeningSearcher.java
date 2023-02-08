package code.screenings.application;

import code.screenings.application.dto.SeatDetails;
import code.screenings.application.dto.ScreeningDto;
import code.screenings.application.dto.SeatDto;
import code.screenings.domain.Screening;
import code.screenings.domain.ScreeningRepository;
import code.screenings.domain.Seat;
import code.screenings.domain.SeatRepository;
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

    private final SeatRepository seatRepository;

    public List<ScreeningDto> searchScreeningsBy(ScreeningSearchParams params) {
        return screeningRepository
                .findBy(params)
                .stream()
                .map(Screening::toDto)
                .toList();
    }

    List<SeatDto> searchSeats(UUID screeningId) {
        return this.seatRepository
                .findByScreening_Id(screeningId)
                .stream()
                .map(Seat::toDto)
                .toList();
    }

    SeatDetails searchScreeningDetails(UUID seatId, Clock clock) {
        var seat = this.seatRepository
                .findById(seatId)
                .orElseThrow(SeatNotFoundException::new);
        var screening = seat.getScreening();
        var timeToScreeningStartInHours = screening.timeToScreeningStartInHours(clock);
        var isFree = seat.isFree();
        return new SeatDetails(
                isFree,
                timeToScreeningStartInHours
        );
    }
}
