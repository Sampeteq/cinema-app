package code.screenings;

import code.screenings.dto.ScreeningDto;
import code.screenings.dto.ScreeningSearchParamsDto;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
class ScreeningSearcher {

    private final ScreeningRepository screeningRepository;

    public List<ScreeningDto> searchBy(ScreeningSearchParamsDto paramsDto) {
        var screeningDate = paramsDto.getScreeningDate()
                != null ? ScreeningDate.of(paramsDto.getScreeningDate())
                : null;
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
