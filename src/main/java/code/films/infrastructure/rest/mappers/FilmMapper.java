package code.films.infrastructure.rest.mappers;

import code.films.infrastructure.rest.dto.FilmDto;
import code.films.domain.Film;
import org.springframework.stereotype.Component;

@Component
public class FilmMapper {

    public FilmDto mapToDto(Film film) {
        return new FilmDto(
                film.getId(),
                film.getTitle(),
                film.getCategory(),
                film.getYear(),
                film.getDurationInMinutes()
        );
    }
}
