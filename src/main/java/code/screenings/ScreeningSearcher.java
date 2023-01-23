package code.screenings;
;
import code.screenings.dto.ScreeningDetails;
import code.screenings.dto.ScreeningDto;
import code.screenings.dto.ScreeningSearchParamsDto;
import code.screenings.dto.SeatDto;
import code.screenings.exception.ScreeningNotFoundException;
import code.screenings.exception.SeatNotFoundException;
import lombok.AllArgsConstructor;

import java.time.Clock;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
class ScreeningSearcher {

    private final ScreeningRepository screeningRepository;

    List<ScreeningDto> searchScreeningsBy(ScreeningSearchParamsDto paramsDto) {
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
