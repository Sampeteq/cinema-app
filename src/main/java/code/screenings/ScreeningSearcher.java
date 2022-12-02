package code.screenings;

import code.screenings.dto.ScreeningDTO;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
class ScreeningSearcher {

    private final ScreeningRepository screeningRepository;

    public List<ScreeningDTO> searchBy(Map<String, Object> params) {
        var filmId = (UUID) params.get("filmId");
        var date = (LocalDateTime) params.get("date");
        var screeningDate = date != null ? ScreeningDate.of(date) : null;
//        var example = Screening
//                .builder()
//                .date(screeningDate)
//                .filmId(filmId)
//                .build();
        return screeningRepository
                .getBy(screeningDate, filmId)
                .stream()
                .map(Screening::toDTO)
                .toList();
    }
}
