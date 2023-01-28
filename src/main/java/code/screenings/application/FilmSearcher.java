package code.screenings.application;

import code.screenings.domain.Film;
import code.screenings.domain.FilmCategory;
import code.screenings.domain.FilmRepository;
import code.screenings.domain.dto.FilmDto;
import code.screenings.domain.dto.FilmSearchParamsDto;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class FilmSearcher {

    private final FilmRepository filmRepository;

    public List<FilmDto> searchFilmsBy(FilmSearchParamsDto paramsDto) {
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
}
