package code.films;

import code.films.dto.FilmDto;
import code.films.dto.FilmSearchParamsDto;
import code.films.exception.FilmNotFoundException;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
class FilmSearcher {

    private final FilmRepository filmRepository;

    List<FilmDto> searchFilms(FilmSearchParamsDto paramsDto) {
        var params = FilmSearchParams
                .builder()
                .category(paramsDto.category == null ? null : FilmCategory.fromDTO(paramsDto.category))
                .build();
        return filmRepository
                .findBy(params)
                .stream()
                .map(Film::toDto)
                .toList();
    }

    int searchFilmDuration(UUID filmId) {
        return filmRepository
                .findById(filmId)
                .map(Film::toDto)
                .map(FilmDto::durationInMinutes)
                .orElseThrow(FilmNotFoundException::new);
    }
}
