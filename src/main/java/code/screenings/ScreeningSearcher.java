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
        return screeningRepository
                .getBy(screeningDate, paramsDto.getFilmId())
                .stream()
                .map(Screening::toDTO)
                .toList();
    }
}
