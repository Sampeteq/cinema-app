package code.films;

import code.films.dto.FilmSearchParamsView;
import code.films.dto.FilmView;
import code.films.exception.FilmNotFoundException;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
class FilmSearcher {

    private final FilmRepository filmRepository;

    List<FilmView> searchFilms(FilmSearchParamsView paramsDto) {
        var params = FilmSearchParams
                .builder()
                .category(paramsDto.category == null ? null : FilmCategory.fromDTO(paramsDto.category))
                .build();
        return filmRepository
                .findBy(params)
                .stream()
                .map(Film::toView)
                .toList();
    }

    int searchFilmDuration(UUID filmId) {
        return filmRepository
                .findById(filmId)
                .map(Film::toView)
                .map(FilmView::durationInMinutes)
                .orElseThrow(() -> new FilmNotFoundException(filmId));
    }
}
