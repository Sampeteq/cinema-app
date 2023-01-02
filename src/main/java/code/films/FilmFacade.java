package code.films;

import code.films.dto.CreateFilmDto;
import code.films.dto.FilmDto;
import code.films.dto.FilmSearchParamsDto;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
public class FilmFacade {

    private final FilmFactory filmFactory;

    private final FilmRepository filmRepository;

    public FilmDto createFilm(CreateFilmDto dto) {
        var film = filmFactory.createFilm(
                dto.title(),
                FilmCategory.fromDTO(dto.filmCategory()),
                dto.year()
        );
        return filmRepository
                .add(film)
                .toDTO();
    }

    public List<FilmDto> searchFilms(FilmSearchParamsDto paramsDto) {
        var params = FilmSearchParams
                .builder()
                .category(paramsDto.category == null ? null : FilmCategory.fromDTO(paramsDto.category))
                .build();
        return filmRepository
                .getBy(params)
                .stream()
                .map(Film::toDTO)
                .toList();
    }

    public boolean isPresent(UUID filmId) {
        return filmRepository.getById(filmId).isPresent();
    }
}
