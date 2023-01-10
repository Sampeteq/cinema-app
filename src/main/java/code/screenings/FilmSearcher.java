package code.screenings;

import code.screenings.dto.FilmView;
import code.screenings.dto.FilmSearchParamsView;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
class FilmSearcher {

    private final FilmRepository filmRepository;

    public List<FilmView> searchFilms(FilmSearchParamsView paramsDto) {
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
}
