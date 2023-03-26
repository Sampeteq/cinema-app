package code.bookings.application.services;

import code.bookings.application.dto.FilmSearchParams;
import code.bookings.application.services.mappers.FilmMapper;
import code.bookings.domain.FilmRepository;
import code.bookings.application.dto.FilmDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class FilmSearchService {

    private final FilmRepository filmRepository;

    private final FilmMapper filmMapper;

    public List<FilmDto> searchFilmsBy(FilmSearchParams params) {
        return filmRepository
                .findBy(params)
                .stream()
                .map(filmMapper::mapToDto)
                .toList();
    }
}
