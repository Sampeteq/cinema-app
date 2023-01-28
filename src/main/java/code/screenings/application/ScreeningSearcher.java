package code.screenings.application;

import code.screenings.domain.Screening;
import code.screenings.domain.ScreeningRepository;
import code.screenings.domain.dto.ScreeningDetails;
import code.screenings.domain.dto.ScreeningDto;
import code.screenings.domain.dto.ScreeningSearchParamsDto;
import code.screenings.domain.dto.SeatDto;
import code.screenings.domain.exception.ScreeningNotFoundException;
import code.screenings.domain.exception.SeatNotFoundException;
import lombok.AllArgsConstructor;

import java.time.Clock;
import java.util.List;
import java.util.UUID;

;

@AllArgsConstructor
public class ScreeningSearcher {

    private final ScreeningRepository screeningRepository;

    public List<ScreeningDto> searchScreeningsBy(ScreeningSearchParamsDto paramsDto) {
        var screeningDate = paramsDto.getScreeningDate();
        var params = ScreeningSearchParams
                .builder()
                .filmId(paramsDto.getFilmId())
                .date(screeningDate)
                .build();
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
