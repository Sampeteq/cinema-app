package code.screenings;

import code.screenings.dto.ScreeningDTO;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Example;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
class ScreeningSearcher {

    private final ScreeningRepository screeningRepository;

    public List<ScreeningDTO> searchBy(Map<String, Object> readParams) {
        var filmId = (UUID) readParams.get("filmId");
        var date = (LocalDateTime) readParams.get("date");
        var screeningBuilder = Screening
                .builder()
                .filmId(filmId);
        if (date != null) {
            var screeningDate = ScreeningDate.of(date);
            screeningBuilder.date(screeningDate);
        }
        var screening = screeningBuilder.build();
        var example = Example.of(screening);
        return screeningRepository
                .findAll(example)
                .stream()
                .map(Screening::toDTO)
                .toList();
    }
}
