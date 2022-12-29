package code.screenings;

import code.screenings.dto.ScreeningDto;
import code.screenings.dto.ScreeningSearchParamsDto;
import lombok.AllArgsConstructor;

import java.util.List;

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
                .getBy(params)
                .stream()
                .map(Screening::toDTO)
                .toList();
    }
}
