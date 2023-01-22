package code.bookings;

import code.bookings.dto.ScreeningDto;
import code.bookings.dto.ScreeningSearchParamsDto;
import code.bookings.dto.SeatDto;
import code.bookings.exception.ScreeningNotFoundException;
import lombok.AllArgsConstructor;

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
}
