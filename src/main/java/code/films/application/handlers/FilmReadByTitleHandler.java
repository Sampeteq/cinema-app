package code.films.application.handlers;

import code.films.application.dto.FilmDto;
import code.films.application.dto.FilmMapper;
import code.films.infrastructure.db.FilmRepository;
import code.shared.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class FilmReadByTitleHandler {

    private final FilmRepository filmRepository;
    private final FilmMapper filmMapper;

    @Transactional(readOnly = true)
    public FilmDto handle(String title) {
        return filmRepository
                .readByTitle(title)
                .map(filmMapper::mapToDto)
                .orElseThrow(() -> new EntityNotFoundException("Film"));
    }
}
