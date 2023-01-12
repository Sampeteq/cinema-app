package code.screenings;

import code.screenings.dto.ScreeningSearchParamsView;
import code.screenings.dto.ScreeningView;
import code.screenings.dto.SeatView;
import code.screenings.exception.ScreeningNotFoundException;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
class ScreeningSearcher {

    private final ScreeningRepository screeningRepository;

    List<ScreeningView> searchBy(ScreeningSearchParamsView paramsDto) {
        var screeningDate = paramsDto.getScreeningDate();
        var params = ScreeningSearchParams
                .builder()
                .filmId(paramsDto.getFilmId())
                .date(screeningDate)
                .build();
        return screeningRepository
                .findBy(params)
                .stream()
                .map(Screening::toView)
                .toList();
    }

    List<SeatView> searchSeatsByScreeningId(UUID screeningId) {
        return screeningRepository
                .findById(screeningId)
                .map(Screening::seatsViews)
                .orElseThrow(() -> new ScreeningNotFoundException(screeningId));
    }
}
