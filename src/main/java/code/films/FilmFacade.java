package code.films;

import code.films.dto.AddFilmDto;
import code.films.dto.FilmDto;
import code.films.dto.FilmSearchParamsDto;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
public class FilmFacade {

    private final FilmRepository filmRepository;

    public FilmDto add(AddFilmDto dto) {
        var film = Film
                .builder()
                .id(UUID.randomUUID())
                .title(dto.title())
                .category(FilmCategory.fromDTO(dto.filmCategory()))
                .year(FilmYear.of(dto.year()))
                .build();
        return filmRepository
                .add(film)
                .toDTO();
    }

    public List<FilmDto> search(FilmSearchParamsDto paramsDto) {
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
