package code.bookings.application.services.mappers;

import code.bookings.application.dto.FilmDto;
import code.bookings.domain.Film;
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
