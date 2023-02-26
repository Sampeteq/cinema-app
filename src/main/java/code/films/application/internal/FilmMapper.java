package code.films.application.internal;

import code.films.application.dto.FilmDto;
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
