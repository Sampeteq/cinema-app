package code.screenings;

import code.screenings.dto.ScreeningDto;
import code.screenings.dto.ScreeningSearchParamsDto;
import code.screenings.dto.SeatDto;
import code.screenings.exception.ScreeningNotFoundException;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
class ScreeningSearcher {

    private final ScreeningRepository screeningRepository;

    List<ScreeningDto> searchBy(ScreeningSearchParamsDto paramsDto) {
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

    List<SeatDto> searchSeatsByScreeningId(UUID screeningId) {
        return screeningRepository
                .findById(screeningId)
                .map(Screening::seatsDtos)
                .orElseThrow(ScreeningNotFoundException::new);
    }
}
