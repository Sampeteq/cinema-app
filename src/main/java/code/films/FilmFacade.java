package code.films;

import code.films.dto.CreateFilmDto;
import code.films.dto.FilmSearchParamsDto;
import code.films.dto.FilmDto;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
public class FilmFacade {

    private final FilmFactory filmFactory;

    private final FilmSearcher filmSearcher;

    public FilmDto createFilm(CreateFilmDto dto) {
        return filmFactory.createFilm(dto).toDto();
    }

    public List<FilmDto> searchFilms(FilmSearchParamsDto paramsDto) {
        return filmSearcher.searchFilms(paramsDto);
    }

    public int searchFilmDuration(UUID filmId) {
        return filmSearcher.searchFilmDuration(filmId);
    }
}
