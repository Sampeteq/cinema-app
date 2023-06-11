package code.films.application.handlers;

import code.films.application.dto.FilmScreeningSeatDto;
import code.films.domain.FilmScreening;
import code.films.application.dto.FilmScreeningSeatMapper;
import code.films.infrastructure.db.FilmScreeningReadOnlyRepository;
import code.shared.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FilmScreeningSeatReadHandler {

    private final FilmScreeningReadOnlyRepository screeningReadOnlyRepository;
    private final FilmScreeningSeatMapper seatMapper;

    @Transactional(readOnly = true)
    public List<FilmScreeningSeatDto> handle(Long screeningId) {
        return screeningReadOnlyRepository
                .findById(screeningId)
                .map(FilmScreening::getSeats)
                .map(seatMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Screening"));
    }
}
