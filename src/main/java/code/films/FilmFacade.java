package code.films;

import code.films.dto.FilmCreatingRequest;
import code.films.dto.FilmSearchParamsView;
import code.films.dto.FilmView;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
public class FilmFacade {

    private final FilmFactory filmFactory;

    private final FilmSearcher filmSearcher;

    public FilmView createFilm(FilmCreatingRequest dto) {
        return filmFactory.createFilm(dto).toView();
    }

    public List<FilmView> searchFilms(FilmSearchParamsView paramsDto) {
        return filmSearcher.searchFilms(paramsDto);
    }

    public int searchFilmDuration(UUID filmId) {
        return filmSearcher.searchFilmDuration(filmId);
    }
}
